import SwiftUI
import Shared

struct HomeScreen: View {
    
    let viewModel = HomeDependencies().with{
        HomeViewModel(
            getOpeningCountDownState: $0.getOpeningCountDownState,
            shouldShowCountdownWidgetSuggestion:$0.shouldShowCountdownWidgetSuggestion,
            getPromotedEvents: $0.getPromotedEventsUseCase,
            getRemoteMessages: $0.getRemoteMessages
        )
    }
    
    @State
    var viewState: HomeViewModel.ViewState = HomeViewModel.ViewState()
    
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(){
                    ForEach(viewState.messages, id: \.self){ message in
                        MessageCard(message: message)
                    }
                    if let countDown = viewState.openingCountDownState as? CountDownState.CountingDown {
                        CountdownCard(
                            days: countDown.daysLeft,
                            hours: countDown.hoursLeft,
                            minutes: countDown.minutesLeft,
                            seconds: countDown.secondsLeft,
                            season: countDown.season
                        )
                    }
                }
                .frame(
                    maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/,
                    maxHeight: .infinity,
                    alignment: .top
                )
                .navigationBarTitle(Res.strings().home_topbar_title.desc().localized(), displayMode: .large)
                .toolbar {
                    ToolbarItem {
                        NavigationLink {
                            SettingsScreen()
                        } label: {
                            Image(systemName: "gear")
                        }
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
    HomeScreen()
}

struct MessageCard: View {
    var message: DtoMessage
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                let icon = switch(message.type) {
                case .info:
                    "info.circle.fill"
                case .warning:
                    "exclamationmark.triangle.fill"
                default: "ellipsis.message.fill"
                }
                Image(systemName: icon)
                Text(message.title.forCurrentLanguage()).font(.title2)
            }
            Text(message.content.forCurrentLanguage())
            ForEach(message.buttons, id: \.self) { button in
                Button(button.title.forCurrentLanguage()) {
                    if let url = URL(string: button.url.forCurrentLanguage()) {
                        UIApplication.shared.open(url)
                    }
                }
                .buttonStyle(.bordered)
                .frame(maxWidth: .infinity, alignment: .trailing)
            }
        }
        .padding()
        .background(.thinMaterial)
        .cornerRadius(24.0)
        .padding()
    }
}
