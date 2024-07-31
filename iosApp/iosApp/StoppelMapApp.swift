import SwiftUI
import Shared

@main
struct StoppelMapApp: App {
    
    init() {
        KoinKt.doInitKoin(modules: [dateTimeModule, appModule, iosCountdownModule])
        
        Task {
            try! await DataUpdateDependencies().updateRemoteAppConfigUseCase.invoke()
            try! await DataUpdateDependencies().updateDataUseCase.invoke()
            try! await NewsDependencies().loadLatestNews.invoke()
        }
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
