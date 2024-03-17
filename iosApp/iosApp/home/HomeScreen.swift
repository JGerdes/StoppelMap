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
                        minutes: countDown.minutesLeft
                    )
                }
            }
            .frame(
                maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/,
                maxHeight: .infinity,
                alignment: .top
            )
            .navigationBarTitle("Stoppelmarkt 2024", displayMode: .large)
        }.task {
            for await state in viewModel.state {
                viewState = state
            }
        }
    }
}


#Preview {
    HomeScreen()
}
