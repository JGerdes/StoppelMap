import SwiftUI

struct ContentView: View {
    var body: some View {
        TabView{
            HomeScreen()
                .tabItem{
                    Label("Info",systemImage: "house.fill")
                }
            Text("Karte")
                .tabItem{
                    Label("Karte",systemImage: "map.fill")
                }
            Text("Programm")
                .tabItem{
                    Label("Programm",systemImage: "calendar")
                }
            Text("Anreise")
                .tabItem{
                    Label("Anreise",systemImage: "bus.fill")
                }
        }
    }
}


#Preview {
    ContentView()
}
