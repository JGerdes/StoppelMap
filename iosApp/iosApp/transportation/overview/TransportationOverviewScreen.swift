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

    
    var body: some View {
        NavigationStack {
            TabView(selection: $selectedTab) {
                ForEach(TransportationType.allCases, id: \.self) { tab in
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
                    } else {
                        Text("Taxi").tag(tab)
                    }
                }
            }
            .tabViewStyle(.page(indexDisplayMode: .never))
            .animation(.easeOut(duration: 0.2), value: selectedTab)
            .transition(.slide)
            .navigationBarTitle(Res.strings().transportation_overview_topbar_title.desc().localized(), displayMode: .inline)
            .safeAreaInset(edge: .top) {
                Picker(selection: $selectedTab, label: Text("")) {
                    ForEach(TransportationType.allCases, id: \.self) { tab in
                        Text(tab.titleStringDesc.localized()).tag(tab)
                    }
                }
                .pickerStyle(SegmentedPickerStyle())
                .padding(.horizontal)
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
