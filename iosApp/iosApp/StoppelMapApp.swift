import SwiftUI
import Shared

@main
struct StoppelMapApp: App {
    
    init() {
        KoinKt.doInitKoin(modules: [dateTimeModule, appModule])
        let deps = DataUpdateDependencies()
        Task {
            try! await deps.copyAssetDataFilesUseCase.invoke(
                databaseAsset: Res.assets.shared.database,
                mapdataAsset: Res.assets.shared.mapdata
            )
            try! await deps.updateAppConfigAndDownloadFilesUseCase.invoke()
        }
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
