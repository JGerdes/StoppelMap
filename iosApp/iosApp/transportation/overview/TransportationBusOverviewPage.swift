import SwiftUI
import Shared

struct TransportationBusOverviewPage: View {
    
    var viewState: TransportationOverviewViewModel.BusRoutesState
    var onRouteTap: (String) -> Void
    
    var body: some View {
        List {
            ForEach(viewState.routes, id: \.slug) { route in
                NavigationLink {
                    RouteScreen(routeId: route.slug)
                        .navigationTitle(route.name)
                } label: {
                    Text(route.name)
                }.buttonStyle(PlainButtonStyle())
            }
        }.listStyle(.plain)
    }
}
