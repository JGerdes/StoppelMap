import WidgetKit
import SwiftUI
import Shared

@main
struct CountdownWidgetBundle: WidgetBundle {
    
    init() {
        KoinKt.doInitKoin(modules: [dateTimeModule, appModule, iosCountdownModule])
    }
    
    var body: some Widget {
        CountdownWidget()
    }
}
