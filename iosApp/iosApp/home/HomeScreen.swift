import SwiftUI
import Shared

struct HomeScreen: View {
    
    let viewModel = HomeViewModel2(
        getOpeningCountDownState: HomeDependencies().getCountDownStateUseCase
    )
    
    @State
    var viewState: HomeViewModel2.ViewState = HomeViewModel2.ViewState()
    
    var body: some View {
        NavigationStack {
            VStack(){
                if let countDown = viewState.countDownState as? CountDownState.CountingDown {
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
