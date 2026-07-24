import SwiftUI
import Shared

struct HomeScreen: View {
    
    let viewModel = HomeDependencies().with{
        HomeViewModel(
            getOpeningCountDownState: $0.getOpeningCountDownState,
            shouldShowCountdownWidgetSuggestion:$0.shouldShowCountdownWidgetSuggestion,
            getPromotedEvents: $0.getPromotedEventsUseCase,
            getRemoteMessages: $0.getRemoteMessages,
            getHomeCards: $0.getHomeCardsUseCase,
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
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .background(.thinMaterial)
                                .cornerRadius(24.0)
                                .padding(.horizontal)
                            }
                        }
                        ForEach(viewState.cards, id: \.id){ card in
                            switch card {
                            case let contentCard as DtoHomeCardContent:
                                ContentCard(card: contentCard)
                            default:
                                // TODO: Handle other HomeCard types
                                Text("Unsupported")
                            }
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
                .frame(maxWidth: .infinity, alignment: .leading)
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
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(.thinMaterial)
        .cornerRadius(24.0)
        .padding(.horizontal)
    }
}


struct ContentCard: View {
    
    @Environment(\.colorScheme) private var colorScheme
    
    var card: DtoHomeCardContent
    var feedbackUrl: String?
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            let headerImage = (colorScheme == .dark ? card.headerImageDark : card.headerImage) ?? card.headerImage
            if let image = headerImage, let url = URL(string: image.url) {
                AsyncImage(url: url) { result in
                    result.image?.resizable()
                        .scaledToFill()
                }
            }
            VStack(alignment: .leading) {
                if let title = card.title {
                    Text(title.forCurrentLanguage()).font(.title2)
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
                Text(card.text.forCurrentLanguage())
                    .frame(maxWidth: .infinity, alignment: .leading)
                if let buttons = card.buttons {
                    ForEach(buttons, id: \.self) { button in
                        Button {
                            let action = switch button.action {
                            case let openUrl as DtoHomeCardContentContentButtonActionOpenUrl: {
                                if let url = URL(string: openUrl.url.forCurrentLanguage()) {
                                    UIApplication.shared.open(url)
                                }
                            }
                            case let callNumber as DtoHomeCardContentContentButtonActionCallPhoneNumber: {
                                if let url = URL(string: "tel:\(callNumber.phoneNumber)") {
                                    UIApplication.shared.open(url   )
                                }
                            }
                            case is DtoHomeCardContentContentButtonActionSendFeedback: {
                                if let urlString = feedbackUrl, let url = URL(string: urlString) {
                                    UIApplication.shared.open(url)
                                }
                            }
                            default: {}
                            }
                            action()
                        } label: {
                            if let image = iconName(for: button.icon) {
                                Label(button.label.forCurrentLanguage(), image: image)
                            } else {
                                Text(button.label.forCurrentLanguage())
                            }
                        }.if(
                            button.type == .primary) { view in
                                view.buttonStyle(.borderedProminent)
                            } else: { view in
                                view.buttonStyle(.bordered)
                            }
                    }
                }
            }
            .frame(maxWidth: .infinity)
            .padding()
        }
        .frame(maxWidth: .infinity)
        .background(.thinMaterial)
        .cornerRadius(24.0)
        .padding(.horizontal)
    }
    
    private func iconName(for icon: DtoHomeCardIcon?) -> String? {
        switch icon {
        case .phone:
            return "phone.fill"
        case .insta:
            return "inst"
        case .bsky:
            return "bsky"
        case .masto:
            return "masto"
        case .ghub:
            return "ghub"
        default:
            return nil
        }
    }
        
}
