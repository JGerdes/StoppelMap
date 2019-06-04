package com.jonasgerdes.stoppelmap.news.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.updatePadding
import com.jonasgerdes.stoppelmap.core.util.observe
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.news.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : BaseFragment(R.layout.fragment_news) {

    private val viewModel: NewsViewModel by viewModel()
    private val articleAdapter = GroupAdapter<ViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setOnApplyWindowInsetsListener { v, insets ->
            insets.apply { toolbar.updatePadding(top = insets.systemWindowInsetTop) }
        }
        toolbar.requestApplyInsets()

        articleList.adapter = articleAdapter

        observe(viewModel.articles) { articles ->
            Log.d("NewsFragment:", "got articles: ${articles.size}")
            articleAdapter.update(articles.map { article ->
                ArticleWithoutImagesItem(article)
            })
        }

        observe(viewModel.loadingState) { state ->
            when (state) {
                LoadingState.Idle -> refreshLayout.isRefreshing = false
                LoadingState.Loading -> refreshLayout.isRefreshing = true
                LoadingState.Error.Unknown -> refreshLayout.isRefreshing = false
                LoadingState.Error.NoNetwork -> refreshLayout.isRefreshing = false
            }
        }

        refreshLayout.setOnRefreshListener {
            viewModel.loadMoreArticles()
        }

    }
}