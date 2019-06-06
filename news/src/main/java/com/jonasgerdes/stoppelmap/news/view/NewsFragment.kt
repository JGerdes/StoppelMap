package com.jonasgerdes.stoppelmap.news.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.updatePadding
import com.google.android.material.snackbar.Snackbar
import com.jonasgerdes.androidutil.navigation.onScrolledToEnd
import com.jonasgerdes.stoppelmap.core.util.observe
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.news.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : BaseFragment(R.layout.fragment_news) {

    private val viewModel: NewsViewModel by viewModel()
    private val articleAdapter = GroupAdapter<ViewHolder>()
    private val articleSection = Section()
    private var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setOnApplyWindowInsetsListener { v, insets ->
            insets.apply { toolbar.updatePadding(top = insets.systemWindowInsetTop) }
        }
        toolbar.requestApplyInsets()

        articleList.adapter = articleAdapter.apply {
            add(articleSection)
        }

        observe(viewModel.articles) { articles ->
            Log.d("NewsFragment:", "got articles: ${articles.size}")
            articleSection.update(articles.map { article ->
                ArticleWithoutImagesItem(article)
            })
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

        refreshLayout.setOnRefreshListener {
            viewModel.refreshArticles()
        }


    }

    private fun showSnackbar(@StringRes title: Int) {
        view?.let { snackRoot ->
            snackbar = Snackbar.make(snackRoot, title, Snackbar.LENGTH_LONG).apply {
                show()
            }
        }
    }
}