import SwiftUI
import Shared

struct TransportationBusOverviewPage: View {
    
    var viewState: TransportationOverviewViewModel.BusRoutesState
    var onRouteTap: (String) -> Void
    
    var body: some View {
        List {
            ForEach(viewState.routes, id: \.self.id) { route in
                NavigationLink {
                    RouteScreen(routeId: route.id)
                        .navigationTitle(route.title)
                } label: {
                    VStack(alignment: .leading) {
                        Text(route.title).font(.headline)
                        let via = Res.strings().transportation_overview_card_via.desc().localized()
                        let stations = route.viaStations.joined(separator: " - ")
                        Text(via + " " + stations).font(.caption2).lineLimit(1)
                    }
                }.buttonStyle(PlainButtonStyle())
            }
        }.listStyle(.plain)
    }
}
