import SwiftUI
import Shared

struct ContentView: View {
    
    let getUnreadNewsCount = NewsDependencies().getUnreadNewsCount
    
    @State
    var unreadNewsCount: Int = 0
    
    var body: some View {
        TabView{
            Group {
                HomeScreen()
                    .tabItem{
                        Label(Res.strings().main_bottom_nav_item_home.desc().localized(), systemImage: "house.fill")
                    }
                MapScreen()
                    .ignoresSafeArea()
                    .tabItem{
                        Label(Res.strings().main_bottom_nav_item_map.desc().localized(), systemImage: "map.fill")
                    }
                ScheduleScreen()
                    .tabItem{
                        Label(Res.strings().main_bottom_nav_item_schedule.desc().localized(), systemImage: "calendar")
                    }
                TransportationOverviewScreen()
                    .tabItem{
                        Label(Res.strings().main_bottom_nav_item_transport.desc().localized(), systemImage: "bus.fill")
                    }
                NewsScreen()
                    .tabItem {
                        Label(Res.strings().main_bottom_nav_item_news.desc().localized(), systemImage: "newspaper")
                    }
                    .badge(unreadNewsCount)
            }
            .toolbarBackground(.ultraThinMaterial, for: .tabBar)
            .toolbarBackground(.visible, for: .tabBar)
        }
        .task {
            for await count in getUnreadNewsCount.invoke() {
                unreadNewsCount = count.intValue
            }
        }
    }
}


#Preview {
    ContentView()
}
