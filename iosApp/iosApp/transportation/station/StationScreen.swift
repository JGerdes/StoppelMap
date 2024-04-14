import SwiftUI
import Shared

struct StationScreen: View {
    
    var viewModel: StationViewModel
    
    init(stationId: String) {
        self.viewModel =  TransportationDependencies().with {
            StationViewModel(
                stationId: stationId,
                busRoutesRepository: $0.busRoutesRepository, transportationUserDataRepository: $0.transportationUserDataRepository, createTimetable: $0.createTimetable)
        }
    }
    
    @State
    var viewState: StationViewModel.ViewState = StationViewModel.ViewState()
    
    var body: some View {
        NavigationStack {
            if let state = viewState.stationState as? StationViewModel.StationStateLoaded {
                ScrollView {
                    VStack {
                        //                        ForEach(state.timetable.daySegments, id: \.self.type) { segment in
                        //                            Section {
                        //                                ForEach(segment.departureSlots, id: \.self) { slot in
                        //                                    HStack {
                        //                                        ForEach(slot.test(), id: \.self) { time in
                        //                                            Text(time).frame(maxWidth: .infinity)
                        //                                        }
                        //                                    }
                        //                                    .fixedSize(horizontal: true, vertical: true)
                        //                                    .frame(maxWidth: .infinity)
                        //                                }
                        //
                        //                            }
                        //                        }
                        Grid {
                            GridRow {
                                ForEach(state.timetable.departureDays, id: \.self) { day in
                                    Text(day.dayOfWeek.toStringResource().desc().localized()).font(.headline)
                                }
                            }.padding(.bottom)
                            ForEach(state.timetable.daySegments, id: \.self.type) { segment in
                                Section(header: Text(segment.type.toStringResource().desc().localized()).padding(.bottom, 8).padding(.top)) {
                                    ForEach(segment.departureSlots, id: \.self) { slot in
                                        GridRow {
                                            ForEach(slot.formattedTimes(), id: \.self) {time in
                                                Text(time)
                                            }
                                        }
                                    }
                                    
                                }
                            }.frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/)
                        }.frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/)
                    }
                }.navigationTitle(state.stationTitle)
            } else {
                Text("Lade Daten")
            }
        }.task {
            for await state in viewModel.state {
                viewState = state
            }
        }
    }
}
