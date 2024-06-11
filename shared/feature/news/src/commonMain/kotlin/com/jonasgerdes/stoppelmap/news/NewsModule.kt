import com.jonasgerdes.stoppelmap.news.data.NewsRepository
import com.jonasgerdes.stoppelmap.news.data.remote.RemoteNewsSource
import org.koin.dsl.module

val newsModule = module {
    single {
        RemoteNewsSource(
            baseUrl = "http://192.168.178.20:8080",
            httpClient = get()
        )
    }

    single {
        NewsRepository(remoteNewsSource = get())
    }
}