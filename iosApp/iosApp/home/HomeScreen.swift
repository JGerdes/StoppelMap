import SwiftUI
import Shared

struct HomeScreen: View {
    
    let viewModel = HomeDependencies().with{
        HomeViewModel(
            getOpeningCountDownState: $0.getOpeningCountDownState,
            shouldShowCountdownWidgetSuggestion:$0.shouldShowCountdownWidgetSuggestion,
            getPromotedEvents: $0.getPromotedEventsUseCase,
            getRemoteMessages: $0.getRemoteMessages,
            getFeedbackEmailUrl: $0.getFeedbackEmailUrl
        )
    }
    
    @State
    var viewState: HomeViewModel.ViewState? = nil //TODO: Replace this with empty constructor call again. No clue why xcode decided it's not working anymore.
    
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(){
                    if let viewState = viewState {
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
                        if let promotedEventsState = viewState.promotedEventsState as? HomeViewModelPromotedEventsStateVisible {
                            Text(Res.strings().home_officalEventCard_title.desc().localized())
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding([.top, .horizontal])
                            ForEach(promotedEventsState.events, id: \.slug) { event in
                                VStack(alignment: .leading, spacing: 10.0) {
                                    Text(event.start.defaultFormat().localized())
                                    if let locationName = event.locationName {
                                        Text(locationName)
                                    }
                                    Text(event.name.localized()).font(.title)
                                    if let eventDescription = event.description_ {
                                        Text(eventDescription.localized()).font(.caption)
                                    }
                                }
                                .padding()
                                .frame(maxWidth: .infinity)
                                .background(.thinMaterial)
                                .cornerRadius(24.0)
                                .padding(.horizontal)
                            }
                        }
                        if let socialState = viewState.instagramPromotionState as? HomeViewModelInstagramPromotionStateVisible {
                            VStack {
                                Text(Res.strings().social_media_promo_instagram_title.desc().localized())
                                Button {
                                    if let url = URL(string: "https://instagram.com/stoppelmap") {
                                        UIApplication.shared.open(url)
                                    }
                                } label: {
                                    Text(Res.strings().social_media_promo_instagram_button.desc().localized())
                                }.buttonStyle(.bordered)
                            }
                            .padding()
                            .frame(maxWidth: .infinity)
                            .background(.thinMaterial)
                            .cornerRadius(24.0)
                            .padding(.horizontal)
                        }
                        if let feedbackState = viewState.feedbackState as? HomeViewModelFeedbackStateVisible {
                            VStack {
                                Text(Res.strings().feedback_card_title.desc().localized())
                                Button {
                                    if let url = URL(string: feedbackState.url) {
                                        UIApplication.shared.open(url)
                                    }
                                } label: {
                                    Text(Res.strings().feedback_card_button.desc().localized())
                                }.buttonStyle(.bordered)
                            }
                            .padding()
                            .frame(maxWidth: .infinity)
                            .background(.thinMaterial)
                            .cornerRadius(24.0)
                            .padding(.horizontal)
                        }
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
