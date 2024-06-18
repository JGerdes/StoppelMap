package com.jonasgerdes.stoppelmap.news

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import app.cash.sqldelight.db.SqlDriver
import com.jonasgerdes.stoppelmap.base.contract.PreferencesPathFactory
import com.jonasgerdes.stoppelmap.news.data.NewsRepository
import com.jonasgerdes.stoppelmap.news.data.local.database.LocalNewsSource
import com.jonasgerdes.stoppelmap.news.data.local.database.localDateAdapter
import com.jonasgerdes.stoppelmap.news.data.remote.RemoteNewsSource
import com.jonasgerdes.stoppelmap.news.database.model.Article
import com.jonasgerdes.stoppelmap.news.database.model.NewsDatabase
import com.jonasgerdes.stoppelmap.news.usecase.GetUnreadNewsCountUseCase
import com.jonasgerdes.stoppelmap.news.usecase.LoadLatestNewsUseCase
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope
import org.koin.dsl.module

expect fun Scope.createDriver(fileName: String): SqlDriver

val newsModule = module {

    single {
        LocalNewsSource(
            newsDatabase = get(),
        )
    }
    single {
        RemoteNewsSource(
            baseUrl = "https://api.stoppelmap.de",
            httpClient = get()
        )
    }

    single {
        NewsRepository(
            localNewsSource = get(),
            remoteNewsSource = get(),
            userDataStore = PreferenceDataStoreFactory.createWithPath(
                corruptionHandler = null,
                migrations = emptyList(),
                produceFile = { get<PreferencesPathFactory>().create("newsUserData").toPath() },
            )
        )
    }

    single {
        NewsDatabase(
            createDriver("news.db"), articleAdapter = Article.Adapter(
                publishedOnAdapter = localDateAdapter
            )
        )
    }

    factory {
        LoadLatestNewsUseCase(newsRepository = get())
    }

    factory {
        GetUnreadNewsCountUseCase(newsRepository = get())
    }
}