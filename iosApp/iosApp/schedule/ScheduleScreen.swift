import SwiftUI
import Shared

struct ScheduleScreen: View {
    
    let viewModel = ScheduleDependencies().with{
        ScheduleViewModel(
            getScheduleDays: $0.getScheduleDaysUseCase,
            eventRepository: $0.eventRepository,
            clockProvider: $0.clockProvider,
            getBookmarkedEvents: $0.getBookmarkedEventsUseCase
        )
    }
    
    @State
    var viewState: ScheduleViewModel.ViewState = ScheduleViewModel.ViewState()
    
    @State private var selection = 0
    
    @State private var showBookmarkSheet = false
    
    var body: some View {
        NavigationStack {
            VStack(spacing: 0) {
                if(!viewState.scheduleDays.isEmpty) {
                    TabView(selection: $selection) {
                        ForEach(0..<viewState.scheduleDays.count, id: \.self) { index in
                            EventPage(
                                scheduleDay: viewState.scheduleDays[index],
                                selectedEvent: viewState.selectedEvent,
                                onEventTap: { event in
                                    viewModel.onEventTap(event: event)
                                },
                                onNotificationToggle: {(event: Event, isActive: Bool) in
                                    viewModel.onEventNotificationSchedule(
                                        eventSlug: event.slug,
                                        notificationActive: isActive
                                    )
                                }
                            ).tag(index)
                        }
                    }
                    .tabViewStyle(.page(indexDisplayMode: .never))
                    .animation(.easeOut(duration: 0.2), value: selection)
                    .transition(.slide)
                    .navigationBarTitle(Res.strings().schedule_topbar_title.desc().localized(), displayMode: .inline)
                    .safeAreaInset(edge: .top) {
                        Picker(selection: $selection, label: Text("")) {
                            ForEach(0..<viewState.scheduleDays.count, id: \.self) { index in
                                let date = viewState.scheduleDays[index].date
                                Text(date.dayOfWeek.toShortResourceString().desc().localized()).tag(index)
                            }
                        }
                        .pickerStyle(SegmentedPickerStyle())
                        .padding(.horizontal)
                    }
                }
            }.sheet(isPresented: $showBookmarkSheet, content: {
                VStack {
                    if let bookmarkedEvents = viewState.bookmarkedEvents as? BookmarkedEventsSome {
                        Text(Res.strings().schedule_bookmarked_list_title.desc().localized())
                            .font(.title)
                            .padding()
                        List {
                            Section {
                                ForEach(bookmarkedEvents.upcoming, id: \.slug) {event in
                                    VStack(alignment: .leading, spacing: 5.0) {
                                        Text(event.start.defaultFormat().localized()).font(.subheadline)
                                        if let locationName = event.locationName {
                                            Text(locationName).font(.subheadline)
                                        }
                                        Text(event.name.localized()).font(.headline)
                                        if let eventDescription = event.description_ {
                                            Text(eventDescription.localized()).font(.footnote)
                                        }
                                    }.frame(maxWidth: .infinity, alignment: .leading)
                                }
                            }.frame(maxWidth: .infinity, alignment: .leading)
                            if(!bookmarkedEvents.past.isEmpty) {
                                Section(Res.strings().schedule_bookmarked_list_past.desc().localized()) {
                                    ForEach(bookmarkedEvents.past, id: \.slug) {event in
                                        VStack(alignment: .leading) {
                                            Text(event.start.defaultFormat().localized()).font(.subheadline)
                                            if let locationName = event.locationName {
                                                Text(locationName).font(.subheadline)
                                            }
                                            Text(event.name.localized()).font(.headline)
                                            if let eventDescription = event.description_ {
                                                Text(eventDescription.localized()).font(.footnote)
                                            }
                                        }
                                        .frame(maxWidth: .infinity, alignment: .leading)
                                    }
                                }.frame(maxWidth: .infinity, alignment: .leading)
                            }
                        }
                    }
                }
                //.presentationDetents([.medium, .large])
                //.presentationDragIndicator(.visible)
                .apply {
                    if #available(iOS 16.4, *) {
                        $0.presentationBackgroundInteraction(.enabled)
                    } else {
                        $0
                    }
                }
            })
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button(Res.strings().schedule_bookmarked_list_title.desc().localized()) {
                        showBookmarkSheet = true
                    }
                }
            }
        }
        .task {
            for await state in viewModel.state {
                viewState = state
            }
        }
    }
}

#Preview {
    ScheduleScreen()
}
