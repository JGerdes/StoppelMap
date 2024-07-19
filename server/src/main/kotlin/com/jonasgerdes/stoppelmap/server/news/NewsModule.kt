package com.jonasgerdes.stoppelmap.server.news

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.jonasgerdes.stoppelmap.server.config.ServerConfig
import com.jonasgerdes.stoppelmap.server.data.instantAdapter
import com.jonasgerdes.stoppelmap.server.data.localDateAdapter
import com.jonasgerdes.stoppelmap.server.news.data.Article
import com.jonasgerdes.stoppelmap.server.news.data.ArticleRepository
import com.jonasgerdes.stoppelmap.server.news.data.ImageRepository
import com.jonasgerdes.stoppelmap.server.news.data.NewsDatabase
import org.koin.dsl.module
import java.io.File

val newsModule = module {
    single {
        val sqliteDirectory = File(get<ServerConfig>().sqliteDirectory).also { it.mkdirs() }
        val sqliteFile = File(sqliteDirectory, "news.db")
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${sqliteFile.absolutePath}")
        if (!sqliteFile.exists()) NewsDatabase.Schema.create(driver)
        NewsDatabase(
            driver = driver,
            articleAdapter = Article.Adapter(
                publishedOnAdapter = localDateAdapter,
                createdAtAdapter = instantAdapter,
                modifiedAtAdapter = instantAdapter,
            )
        )
    }

    single<ArticleRepository> { ArticleRepository(get<NewsDatabase>().articleQueries) }
    single<ImageRepository> { ImageRepository(get<NewsDatabase>().imageQueries) }

    single { NewsController(articleRepository = get(), imageRepository = get(), config = get()) }
}