package com.jonasgerdes.stoppelmap.news

import com.jonasgerdes.stoppelmap.news.ui.NewsViewModel
import com.jonasgerdes.stoppelmap.news.ui.navigation.NewsNavigationIconViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val androidNewsModule = module {
    viewModel {
        NewsViewModel(
            newsRepository = get()
        )
    }

    viewModel {
        NewsNavigationIconViewModel(
            getUnreadNewsCount = get()
        )
    }
}
