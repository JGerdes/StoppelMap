import SwiftUI
import Shared

struct SelectedStation: Identifiable {
    var id: String
}

struct RouteScreen: View {
    
    var viewModel: RouteViewModel
    
    init(routeId: String) {
        self.viewModel = TransportationDependencies().with {
            RouteViewModel(
                routeId: routeId,
                busRoutesRepository: $0.busRoutesRepository,
                clockProvider: $0.clockProvider,
                getNextDepartures: $0.getNextDepartures
            )
        }
    }
    
    @State
    var viewState: RouteViewModel.ViewState = RouteViewModel.ViewState()
    
    @State
    var selectedStation: SelectedStation?
    
    var body: some View {
        NavigationStack {
            if let state = viewState.routeState as? RouteViewModel.RouteStateLoaded {
                RouteDetailView(
                    routeDetails: state.routeDetails,
                    onStationTap: { stationId in
                        selectedStation = SelectedStation(id: stationId)
                    }
                )
            } else {
                Text("Lade Route")
            }
        }
        .sheet(item: $selectedStation) { item in
            StationScreen(stationId: item.id)
                .presentationDragIndicator(.visible)
        }
        .task {
            for await state in viewModel.state {
                viewState = state
            }
        }
    }
}

struct RouteDetailView: View {
    
    var routeDetails: BusRouteDetails
    var onStationTap: (String) -> Void
    
    var body: some View {
        List {
            Section("Haltestellen") {
                ForEach(Array(routeDetails.stations.enumerated()), id: \.element.id) { index, station in
                    HStack {
                        ZStack {
                            VStack(spacing: 0) {
                                Rectangle().frame(width: 2)
                                    .opacity(index == 0 ? 0 : 1)
                                Rectangle().frame(width: 2)
                                    .opacity(index == routeDetails.stations.endIndex-1 ? 0 : 1)
                                
                            }
                            Circle().frame(width: 8, height: 16)
                        }
                        if let stop = station as? BusRouteDetails.StationStop {
                            Text(stop.title).padding([.vertical, .leading])
                            Spacer()
                            if let nextDeparture = stop.nextDepartures.first {
                                Text(nextDeparture.getFormattedStringRes().localized())
                                    .font(.callout)
                            }
                        } else if let destination = station as? BusRouteDetails.StationDestination {
                            Text(destination.title)
                                .frame(maxWidth: .infinity ,alignment: /*@START_MENU_TOKEN@*/.center/*@END_MENU_TOKEN@*/)
                        }
                    }
                    .contentShape(Rectangle())
                    .onTapGesture {
                        onStationTap(station.id)
                    }
                    .listRowSeparator(.hidden)
                }.listRowInsets(EdgeInsets.init(top: 0, leading: 16, bottom: 0, trailing: 16))
            }
        }
        
    }
}

extension Binding where Value == String? {
    public func isSet() -> Binding<Bool> {
        return Binding<Bool>(get:{ self.wrappedValue != nil},
                             set: { if($0 == false){ self.wrappedValue = nil}})
    }
}

#Preview {
    RouteDetailView(routeDetails: BusRouteDetails(routeId: "id", title: "Route", additionalInfo: nil, stations: [
        BusRouteDetails.StationStop(
            id: "stop-1",
            title: "Stop 1",
            routeName: "Route",
            nextDepartures: [
                BusRouteDetails.DepartureTimeImmediately(),
            ], annotateAsNew: false
        ),
        BusRouteDetails.StationStop(
            id: "stop-2",
            title: "Haltestelle 2",
            routeName: "Route",
            nextDepartures: [
                BusRouteDetails.DepartureTimeToday(time: Kotlinx_datetimeLocalTime(hour: 22, minute: 22, second: 0, nanosecond: 0)),
            ], annotateAsNew: false
        ),
        BusRouteDetails.StationStop(
            id: "stop-3",
            title: "Very very long stop name",
            routeName: "Route",
            nextDepartures: [
                BusRouteDetails.DepartureTimeToday(time: Kotlinx_datetimeLocalTime(hour: 22, minute: 33, second: 0, nanosecond: 0)),
            ], annotateAsNew: true
        ),
        
    ], returnStations: []), onStationTap: {_ in})
}
