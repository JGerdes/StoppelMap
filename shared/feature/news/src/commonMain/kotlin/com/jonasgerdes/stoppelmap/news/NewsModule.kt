import com.jonasgerdes.stoppelmap.news.data.NewsRepository
import com.jonasgerdes.stoppelmap.news.data.remote.RemoteNewsSource
import org.koin.dsl.module

val newsModule = module {
    single {
        RemoteNewsSource(
            baseUrl = "https://api.stoppelmap.de",
            httpClient = get()
        )
    }

    single {
        NewsRepository(remoteNewsSource = get())
    }
}