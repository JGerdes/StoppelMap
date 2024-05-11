import SwiftUI
import Shared

struct HomeScreen: View {
    
    let viewModel = HomeDependencies().with{
        HomeViewModel(
            getOpeningCountDownState: $0.getOpeningCountDownState,
            shouldShowCountdownWidgetSuggestion: $0.shouldShowCountdownWidgetSuggestion,
            getNextOfficialEvent: $0.getNextOfficialEvent,
            getNextBookmarkedEvent: $0.getNextBookmarkedEvent,
            getRemoteMessages: $0.getRemoteMessages
        )
    }
    
    @State
    var viewState: HomeViewModel.ViewState = HomeViewModel.ViewState()
    
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(){
                    if let countDown = viewState.openingCountDownState as? CountDownState.CountingDown {
                        CountdownCard(
                            days: countDown.daysLeft,
                            hours: countDown.hoursLeft,
                            minutes: countDown.minutesLeft,
                            seconds: countDown.secondsLeft,
                            year: countDown.year
                        )
                    }
                    Text("title").font(.title)
                    Text("title2").font(.title2)
                    Text("title3").font(.title3)
                    Text("body").font(.body)
                    Text("headline").font(.headline)
                    Text("caption").font(.caption)
                    Text("caption2").font(.caption2)
                    Text("subheadline").font(.subheadline)
                    Text("callout").font(.callout)
                }
                .frame(
                    maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/,
                    maxHeight: .infinity,
                    alignment: .top
                )
                .navigationBarTitle(Res.strings().home_topbar_title.desc().localized(), displayMode: .large)
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
    HomeScreen()
}
