import SwiftUI

struct ContentView: View {
    var body: some View {
        VStack {
            Image(systemName: "figure.dance")
                .imageScale(.large)
                .foregroundStyle(.tint)
            Text("Hallo Stoppelmarkt!")
        }
        .padding()
    }
}

#Preview {
    ContentView()
}
