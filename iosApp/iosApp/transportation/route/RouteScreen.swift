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
            if let info = routeDetails.additionalInfo {
                Text(info)
            }
            Section("Haltestellen") {
                let stations = routeDetails.stations
                ForEach(0..<stations.count, id: \.self) { index in
                    let station = routeDetails.stations[index]
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
                        Text(station.name).padding([.vertical, .leading])
                        Spacer()
                        if let nextDepartures = station.nextDepartures as? BusRouteDetailsStationNextDeparturesLoaded {
                            if let nextDeparture = nextDepartures.departures.first {
                                Text(nextDeparture.getFormattedStringRes().localized())
                                    .font(.callout)
                            }
                        }
                    }
                    .contentShape(Rectangle())
                    .onTapGesture {
                        onStationTap(station.slug)
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

