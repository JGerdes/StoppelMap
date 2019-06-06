package com.jonasgerdes.stoppelmap.news.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        }

        refreshLayout.setOnRefreshListener {
            viewModel.refreshArticles()
        }

        val loadNextThreshold = 1
        articleList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastItem = when (val layoutManager = recyclerView.layoutManager) {
                    is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
                    else -> 0
                }
                recyclerView.adapter?.itemCount?.let { itemCount ->
                    if (itemCount - lastItem <= loadNextThreshold) {
                        viewModel.loadMoreArticles()
                    }
                }
            }
        })

    }
}