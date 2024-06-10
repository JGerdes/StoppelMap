package com.jonasgerdes.stoppelmap.news

import com.jonasgerdes.stoppelmap.news.ui.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidNewsModule = module {
    viewModel {
        NewsViewModel(
            newsRepository = get()
        )
    }
}
