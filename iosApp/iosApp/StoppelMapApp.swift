import SwiftUI
import Shared

@main
struct StoppelMapApp: App {
    
    init() {
        KoinKt.doInitKoin(modules: [dateTimeModule])
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
