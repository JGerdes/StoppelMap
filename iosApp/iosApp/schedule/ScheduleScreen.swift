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
    
    var body: some View {
        Text("Days:" + viewState.scheduleDays.count.formatted())
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
