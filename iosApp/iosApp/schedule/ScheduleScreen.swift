import SwiftUI
import Shared

struct ScheduleScreen: View {
    
    let viewModel = ScheduleDependencies().with{
        ScheduleViewModel(
            eventRepository: $0.eventRepository,
            bookmarkedEventsRepository: $0.bookmarkedEventsRepository,
            clockProvider: $0.clockProvider
        )
    }
    
    @State
    var viewState: ScheduleViewModel.ViewState = ScheduleViewModel.ViewState()
    
    @State private var selection = 0
    
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
                                onNotificationToggle: {(event: DataEvent, isActive: Bool) in
                                    viewModel.onEventNotificationSchedule(
                                        event: event,
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
