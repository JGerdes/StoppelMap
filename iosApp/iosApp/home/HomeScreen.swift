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
