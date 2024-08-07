import SwiftUI
import Shared

struct TransportationBusOverviewPage: View {
    
    var viewState: TransportationOverviewViewModel.BusRoutesState
    var onRouteTap: (String) -> Void
    var onStationTap: (String) -> Void
    
    var body: some View {
        List {
            if(!viewState.favouriteStations.isEmpty) {
                Text(Res.strings().transportation_overview_section_favourite.desc().localized()).font(.caption)
                    .listRowSeparator(.hidden)
            }
            ForEach(viewState.favouriteStations, id: \.slug) { station in
                VStack(alignment: .leading){
                    Text(station.name).padding(.bottom)
                    HStack {
                        Spacer()
                        Text(Res.strings().transportation_route_card_next_departures_label.desc().localized()).font(.caption)
                    }
                    if let nextDepartures = station.nextDepartures as? BusRouteDetailsStationNextDeparturesLoaded {
                        ForEach(nextDepartures.departures, id: \.iOSId) { nextDeparture in
                            HStack{
                                Spacer()
                                Text(nextDeparture.getFormattedStringRes().localized())
                                    .font(.callout)
                            }
                        }
                    }
                    
                }   .padding()
                    .background(.thinMaterial)
                    .cornerRadius(10)
                    .onTapGesture {
                        onStationTap(station.slug)
                    }
                    .frame(maxWidth: .infinity)
                    .listRowSeparator(.hidden)
                
            }
            .frame(maxWidth: .infinity)
            .padding(.bottom)
            
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
