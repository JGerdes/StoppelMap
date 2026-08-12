import SwiftUI
import Shared

struct TransportationOverviewScreen: View {
    
    let viewModel = TransportationDependencies().with {
        TransportationOverviewViewModel(
            busRoutesRepository: $0.busRoutesRepository,
            trainRoutesRepository: $0.trainRoutesRepository,
            taxiServiceRepository: $0.taxiServiceRepository,
            transportationUserDataRepository: $0.transportationUserDataRepository,
            getNextDepartures: $0.getNextDepartures
        )
    }
    
    @State
    var viewState: TransportationOverviewViewModel.ViewState = TransportationOverviewViewModel.ViewState()
    
    @State
    var selectedStation: SelectedStation?
    
    @State private var selectedTab: TransportationType = .bus
    
    @State private var contentWidth: CGFloat = 0

    
    var body: some View {
        let supportedTypes = TransportationType.allCases.filter {type in
            type != .train || viewState.trainRoutesState.routes.isEmpty == false
        }
        NavigationStack {
            VStack(alignment: .leading) {
                DataNoticeBar()
                if(!viewState.favouriteStations.isEmpty) {
                    Text(Res.strings().transportation_overview_section_favourite.desc().localized())
                        .font(.headline)
                        .padding(.horizontal)
                    ScrollView(.horizontal) {
                        HStack(spacing: 0) {
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
                                        selectedStation = SelectedStation(id: station.slug)
                                    }
                                    .frame(minWidth: viewState.favouriteStations.count == 1 ? contentWidth - 20 : contentWidth - 100 )
                                    .padding(10)
                                
                            }
                        }
                    }.onGeometryChange(for: CGSize.self) { proxy in
                        proxy.size
                    } action: {
                        self.contentWidth = $0.width
                    }
                }
                
                TabView(selection: $selectedTab) {
                    ForEach(supportedTypes, id: \.self) { tab in
                        if(tab == TransportationType.bus) {
                            TransportationBusOverviewPage(
                                viewState: viewState.busRoutesViewState,
                                onRouteTap: { routeId in
                                    print("🚏 route: " + routeId)
                                },
                                onStationTap: { stationId in
                                    selectedStation = SelectedStation(id: stationId)
                                }
                            ).tag(tab)
                        } else if(tab == TransportationType.taxi) {
                            TransportationTaxiOverviewPage(viewState: viewState.taxiServicesState).tag(tab)
                        } else if(tab == TransportationType.train) {
                            TransportationTrainOverviewPage(
                                viewState: viewState.trainRoutesState,
                                onRouteTap: { routeId in
                                    print("🚏 route: " + routeId)
                                },
                                onStationTap: { stationId in
                                    selectedStation = SelectedStation(id: stationId)
                                }
                            ).tag(tab)
                        } else {
                            UnderConstrcutionPlaceholder().tag(tab)
                        }
                    }
                }
                .tabViewStyle(.page(indexDisplayMode: .never))
                .animation(.easeOut(duration: 0.2), value: selectedTab)
                .transition(.slide)
                .navigationBarTitle(Res.strings().transportation_overview_topbar_title.desc().localized(), displayMode: .inline)
                .safeAreaInset(edge: .top) {
                    Picker(selection: $selectedTab, label: Text("")) {
                        ForEach(supportedTypes, id: \.self) { tab in
                            Text(tab.titleStringDesc.localized()).tag(tab)
                        }
                    }
                    .pickerStyle(SegmentedPickerStyle())
                    .padding(.horizontal)
                }
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

#Preview {
    TransportationOverviewScreen()
}
