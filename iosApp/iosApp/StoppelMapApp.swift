import SwiftUI
import Shared

@main
struct StoppelMapApp: App {
    
    init() {
        KoinKt.doInitKoin(modules: [dateTimeModule, appModule, iosCountdownModule])
        let deps = DataUpdateDependencies()
        Task {
            try! await deps.copyAssetDataFilesUseCase.invoke(
                databaseAsset: Res.assets.shared.database,
                mapdataAsset: Res.assets.shared.mapdata
            )
            try! await deps.updateAppConfigAndDownloadFilesUseCase.invoke()
        }

        Task {
            try! await NewsDependencies().loadLatestNews.invoke()
        }
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
