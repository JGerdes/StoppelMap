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
    
    @State private var selection = 0
    
    var body: some View {
        NavigationStack {
            if let state = viewState.stationState as? StationViewModel.StationStateLoaded {
                ScrollView {
                    VStack {
                        HStack {
                            Spacer()
                            Button {
                                viewModel.toggleFavourite()
                            } label: {
                                Image(systemName: state.isFavourite ? "star.fill" : "star")
                            }
                            .buttonStyle(.borderedProminent)
                            .accessibilityLabel(Text(
                                state.isFavourite ? Res.strings().transportation_station_topbar_action_unfavourite_contentDescription.desc().localized() : Res.strings().transportation_station_topbar_action_favourite_contentDescription.desc().localized()
                            ))
                            .padding(.horizontal)
                        }
                        
                        if(!state.priceState.prices.isEmpty) {
                            PriceCard(state: state.priceState)
                        }
                        
                        if let info = state.additionalInfo {
                            Text(info)
                                .padding()
                                .background(.thinMaterial)
                                .cornerRadius(10)
                                .padding([.horizontal, .top])
                        }
                        
                        Picker(selection: $selection, label: Text("")) {
                            Text(Res.strings().transportation_station_timetable_outward.desc().localized()).tag(0)
                            if(state.returnTimetable != nil) {
                                Text(Res.strings().transportation_station_timetable_return.desc().localized()).tag(1)
                            }
                        }
                        .pickerStyle(SegmentedPickerStyle())
                        .padding(.horizontal)
                        .padding(.top)
                        
                        if($selection.wrappedValue == 0) {
                            TimetableView(state: state.outwardTimetable)
                        } else if ($selection.wrappedValue == 1) {
                            if let returnTimetable = state.returnTimetable {
                                TimetableView(state: returnTimetable).tag(1)
                            }
                        }
                    }
                }.navigationTitle(state.stationName)
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

struct PriceCard: View {
    
    let state: StationViewModel.PriceState
    let numberFormatter = NumberFormatter()
    
    init(state: StationViewModel.PriceState) {
        self.state = state
        numberFormatter.numberStyle = .decimal
        numberFormatter.maximumFractionDigits = 2
        numberFormatter.minimumFractionDigits = 2
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(Res.strings().transportation_station_prices_title.desc().localized()).font(.caption)
            
            
            ForEach(state.prices, id: \.self.name) { fee in
                HStack {
                    Text(fee.name)
                    Spacer()
                    if let priceText = numberFormatter.string(from: NSNumber(value: Double(fee.price) / 100.0)) {
                        Text(priceText + " €")
                    }
                }.frame(maxWidth: .infinity)
            }
            Text(Res.strings().transportation_station_prices_hint_cash.desc().localized())
                .font(.footnote)
                .padding(.top)
            if(state.showDeutschlandTicketHint) {
                Text(Res.strings().transportation_station_prices_hint_deutschland_ticket.desc().localized())
                    .font(.footnote)
            }
        }
        .padding()
        .background(.thinMaterial)
        .cornerRadius(10)
        .padding([.horizontal, .top])
    }
}


struct TimetableView: View {
    
    var state: Timetable
    
    var body: some View {
        Grid {
            GridRow {
                ForEach(state.departureDays, id: \.self) { day in
                    Text(day.dayOfWeek.toStringResource().desc().localized()).font(.headline)
                }
            }.padding(.vertical)
            ForEach(state.daySegments, id: \.self.type) { segment in
                Section(header: Text(segment.type.toStringResource().desc().localized()).padding(.bottom, 8).padding(.top)) {
                    ForEach(segment.departureSlots, id: \.self) { slot in
                        GridRow {
                            ForEach(slot.formattedTimes(), id: \.self) {formatted in
                                Text(formatted.time)
                                    .opacity(formatted.isInPast ? 0.4 : 1.0)
                            }
                        }.padding(.vertical, 2)
                    }
                    
                }
            }.frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/)
        }.frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/)
            .padding(.bottom)
            .background(.thinMaterial)
            .cornerRadius(10.0)
            .padding(4)
    }
}
