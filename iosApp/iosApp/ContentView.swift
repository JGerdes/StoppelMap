import SwiftUI
import Shared

struct ContentView: View {
    var body: some View {
        TabView{
            Group {
                HomeScreen()
                    .tabItem{
                        Label(Res.strings().main_bottom_nav_item_home.desc().localized(),systemImage: "house.fill")
                    }
                MapScreen()
                    .tabItem{
                        Label(Res.strings().main_bottom_nav_item_map.desc().localized(),systemImage: "map.fill")
                    }
                Text(Res.strings().main_bottom_nav_item_schedule.desc().localized())
                    .tabItem{
                        Label(Res.strings().main_bottom_nav_item_schedule.desc().localized(),systemImage: "calendar")
                    }
                Text(Res.strings().main_bottom_nav_item_transport.desc().localized())
                    .tabItem{
                        Label(Res.strings().main_bottom_nav_item_transport.desc().localized(),systemImage: "bus.fill")
                    }
            }
            .toolbarBackground(.ultraThinMaterial, for: .tabBar)
            .toolbarBackground(.visible, for: .tabBar)
        }
    }
}


#Preview {
    ContentView()
}
