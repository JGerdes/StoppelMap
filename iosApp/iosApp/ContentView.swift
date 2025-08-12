import SwiftUI
import Shared

struct ContentView: View {
    
    let getUnreadNewsCount = NewsDependencies().getUnreadNewsCount
    
    @State
    var unreadNewsCount: Int = 0
    
    @State
    var selectedTab: String = "home"
    
    let deeplinkRepository = MapDependencies().deeplinkRepository
    
    @Environment(\.dismissSearch) var dismissSearch
    
    var body: some View {
        TabView(selection: $selectedTab){
            Group {
                HomeScreen()
                    .tabItem{
                        Label(Res.strings().main_bottom_nav_item_home.desc().localized(), systemImage: "house.fill")
                    }
                    .tag("home")
                MapScreen(onDismissSearch: {dismissSearch()})
                    .tabItem{
                        Label(Res.strings().main_bottom_nav_item_map.desc().localized(), systemImage: "map.fill")
                    }
                    .tag("map")
                ScheduleScreen()
                    .tabItem{
                        Label(Res.strings().main_bottom_nav_item_schedule.desc().localized(), systemImage: "calendar")
                    }
                    .tag("schedule")
                TransportationOverviewScreen()
                    .tabItem{
                        Label(Res.strings().main_bottom_nav_item_transport.desc().localized(), systemImage: "bus.fill")
                    }
                    .tag("transportation")
                NewsScreen()
                    .tabItem {
                        Label(Res.strings().main_bottom_nav_item_news.desc().localized(), systemImage: "newspaper")
                    }
                    .badge(unreadNewsCount)
                    .tag("news")
            }
            .toolbarBackground(.ultraThinMaterial, for: .tabBar)
            .toolbarBackground(.visible, for: .tabBar)
        }
        .task {
            for await count in getUnreadNewsCount.invoke() {
                unreadNewsCount = count.intValue
            }
        }
        .onOpenURL { incomingURL in
            print("🔗 App was opened via URL: \(incomingURL)")
            deeplinkRepository.postDeeplink(url: incomingURL.absoluteString)
            selectedTab = "map"
        }
    }
}


#Preview {
    ContentView()
}
