package com.jonasgerdes.stoppelmap.server.news

import org.koin.dsl.module

val newsModule = module {
    single { NewsController(articleRepository = get(), imageRepository = get(), config = get()) }
}