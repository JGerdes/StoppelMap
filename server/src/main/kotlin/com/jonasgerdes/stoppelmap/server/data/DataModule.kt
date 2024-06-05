package com.jonasgerdes.stoppelmap.server.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.jonasgerdes.stoppelmap.server.Database
import com.jonasgerdes.stoppelmap.server.config.AppConfig
import com.jonasgerdes.stoppelmap.server.news.Article
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single {
        val sqliteDirectory = File(get<AppConfig>().sqliteDirectory).also { it.mkdirs() }
        val sqliteFile = File(sqliteDirectory, "database.db")
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${sqliteFile.absolutePath}")
        if (!sqliteFile.exists()) Database.Schema.create(driver)
        Database(
            driver = driver,
            articleAdapter = Article.Adapter(
                publishedOnAdapter = localDateAdapter,
                createdAtAdapter = instantAdapter,
            )
        )
    }

    single<ArticleRepository> { ArticleRepository(get<Database>().articleQueries) }
}