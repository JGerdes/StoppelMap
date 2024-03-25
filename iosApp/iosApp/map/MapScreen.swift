import SwiftUI

struct MapScreen: View {
    @Environment(\.colorScheme) var colorScheme
    
    var body: some View {
        MapView(colorScheme: colorScheme)
            .ignoresSafeArea(
                edges: .top
            )
    }
}

#Preview {
    MapScreen()
}
