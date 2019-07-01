package com.jonasgerdes.stoppelmap.news.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.updatePadding
import com.google.android.material.snackbar.Snackbar
import com.jonasgerdes.androidutil.navigation.recyclerview.onScrolledToEnd
import com.jonasgerdes.androidutil.view.consumeWindowInsetsTop
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.util.observe
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.news.R
import com.jonasgerdes.stoppelmap.news.data.entity.Article
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : BaseFragment<Route.News>(R.layout.fragment_news) {

    private val viewModel: NewsViewModel by viewModel()
    private val articleAdapter = GroupAdapter<ViewHolder>()
    private val articleSection = Section()
    private var snackbar: Snackbar? = null

    private var shouldScrollToTopOnNewData = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.consumeWindowInsetsTop()

        articleList.adapter = articleAdapter.apply {
            add(articleSection)
        }

        observe(viewModel.articles) { articles ->
            articleSection.update(articles.map { article ->
                if (article.images.isNotEmpty()) ArticleWithImagesItem(
                    article,
                    onMoreClickedListener = { openArticle(it) })
                else ArticleWithoutImagesItem(article, onMoreClickedListener = { openArticle(it) })
            })
            if (articles.isNotEmpty()) {
                if (shouldScrollToTopOnNewData) {
                    articleList.postDelayed({
                        articleList.smoothScrollToPosition(0)
                    }, 1000)
                    shouldScrollToTopOnNewData = false
                }
            }
        }

        observe(viewModel.loadingState) { state ->
            when (state) {
                LoadingState.Idle -> refreshLayout.isRefreshing = false
                LoadingState.Loading.Refresh -> refreshLayout.isRefreshing = true
                LoadingState.Error.Unknown -> refreshLayout.isRefreshing = false
                LoadingState.Error.NoNetwork -> refreshLayout.isRefreshing = false
            }
            when (state) {
                LoadingState.Loading.More -> articleSection.setFooter(LoadingIndicatorItem())
                else -> articleSection.removeFooter()
            }

            when (state) {
                LoadingState.Error.Unknown -> showSnackbar(R.string.news_error_unknown)
                LoadingState.Error.NoNetwork -> showSnackbar(R.string.news_error_no_network)
                else -> snackbar?.also { it.dismiss() }
            }
        }

        articleList.onScrolledToEnd { isOnEnd ->
            if (isOnEnd) viewModel.loadMoreArticles()
        }

        articleAdapter.setOnItemClickListener { item, view ->
            when (item) {
                is ArticleItem -> openArticle(item.article)
                else -> {
                    //do nothing
                }
            }
        }

        refreshLayout.setOnRefreshListener {
            viewModel.refreshArticles()
        }


    }

    private fun openArticle(article: Article) {
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
        )
    }

    private fun showSnackbar(@StringRes title: Int) {
        view?.let { snackRoot ->
            snackbar = Snackbar.make(snackRoot, title, Snackbar.LENGTH_LONG).apply {
                show()
            }
        }
    }

    override fun onReselected() {
        articleList.smoothScrollToPosition(0)
    }

    override fun processRoute(route: Route.News) {
        shouldScrollToTopOnNewData = route.scrollToTop
        if(route.forceReload) viewModel.refreshArticles()
    }
}