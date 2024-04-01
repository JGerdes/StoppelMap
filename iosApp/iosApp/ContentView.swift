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
                ScheduleScreen()
                    .tabItem{
                        Label(Res.strings().main_bottom_nav_item_schedule.desc().localized(),systemImage: "calendar")
                    }
                VStack{
                    Text("title").font(.title)
                    Text("title2").font(.title2)
                    Text("title3").font(.title3)
                    Text("body").font(.body)
                    Text("headline").font(.headline)
                    Text("caption").font(.caption)
                    Text("caption2").font(.caption2)
                    Text("subheadline").font(.subheadline)
                }
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
