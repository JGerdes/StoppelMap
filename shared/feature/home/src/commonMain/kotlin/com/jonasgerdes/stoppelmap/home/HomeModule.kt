import com.jonasgerdes.stoppelmap.home.usecase.GetPromotedEventsUseCase
import com.jonasgerdes.stoppelmap.home.usecase.GetRemoteMessagesUseCase
import org.koin.dsl.module

val homeModule = module {
    factory { GetRemoteMessagesUseCase(appConfigRepository = get(), appInfo = get()) }
    factory { GetPromotedEventsUseCase(eventRepository = get(), clockProvider = get()) }
}