import app.cash.sqldelight.db.SqlDriver
import com.jonasgerdes.stoppelmap.news.data.NewsRepository
import com.jonasgerdes.stoppelmap.news.data.local.database.LocalNewsSource
import com.jonasgerdes.stoppelmap.news.data.local.database.instantAdapter
import com.jonasgerdes.stoppelmap.news.data.local.database.localDateAdapter
import com.jonasgerdes.stoppelmap.news.data.remote.RemoteNewsSource
import com.jonasgerdes.stoppelmap.news.database.model.Article
import com.jonasgerdes.stoppelmap.news.database.model.NewsDatabase
import org.koin.core.scope.Scope
import org.koin.dsl.module

expect fun Scope.createDriver(fileName: String): SqlDriver

val newsModule = module {

    single {
        LocalNewsSource(
            newsDatabase = get(),
            clockProvider = get(),
        )
    }
    single {
        RemoteNewsSource(
            baseUrl = "http://192.168.178.20:8080",
            httpClient = get()
        )
    }

    single {
        NewsRepository(
            localNewsSource = get(),
            remoteNewsSource = get(),
        )
    }

    single {
        NewsDatabase(
            createDriver("news.db"), articleAdapter = Article.Adapter(
                publishedOnAdapter = localDateAdapter,
                readAtAdapter = instantAdapter
            )
        )
    }
}