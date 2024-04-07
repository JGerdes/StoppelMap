import SwiftUI
import Shared

struct TransportationBusOverviewPage: View {
    
    var viewState: TransportationOverviewViewModel.BusRoutesState
    var onRouteTap: (String) -> Void
    
    var body: some View {
        ScrollView(.vertical) {
            LazyVStack {
                ForEach(viewState.routes, id: \.self.id) { route in
                    RouteSummaryCard(
                        route: route,
                        onRouteTap: onRouteTap
                    )
                }
            }
            .padding(.horizontal)
        }
    }
}

struct RouteSummaryCard: View {
    var route: RouteSummary
    
    var onRouteTap: (String) -> Void
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(route.title).font(.headline)
            Spacer()
            Text(Res.strings().transportation_overview_card_via.desc().localized()).font(.caption2)
            ForEach(route.viaStations, id: \.self) {station in
                Text(station)
                    .font(.body)
                    .padding(.leading)
            }
        }
        .padding()
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            alignment: .topLeading
        )
        .contentShape(Rectangle())
        .onTapGesture {
            onRouteTap(route.id)
        }
        .background(.ultraThinMaterial)
        .cornerRadius(8.0)
    }
}
