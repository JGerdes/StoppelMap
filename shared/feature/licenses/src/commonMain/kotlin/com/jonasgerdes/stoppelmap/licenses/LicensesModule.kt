import com.jonasgerdes.stoppelmap.licenses.data.LicensesRepository
import com.jonasgerdes.stoppelmap.licenses.data.commonImageSources
import com.jonasgerdes.stoppelmap.licenses.data.commonLibraries
import org.koin.dsl.module

val licensesModule = module {
    single {
        LicensesRepository(
            libraries = commonLibraries,
            imageSources = commonImageSources,
        )
    }
}