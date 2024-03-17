import SwiftUI
import Shared

@main
struct StoppelMapApp: App {
    
    init() {
        KoinKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
