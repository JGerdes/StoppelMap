import SwiftUI

struct MapScreen: View {
    var body: some View {
        MapView()
            .ignoresSafeArea(
                edges: .top
            )
    }
}

#Preview {
    MapScreen()
}
