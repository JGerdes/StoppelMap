import WidgetKit
import SwiftUI
import Shared

struct Provider: TimelineProvider {
    
    let getOpeningCountDown = CountdownDependencies().getOpeningCountDownUseCase
    
    func placeholder(in context: Context) -> SimpleEntry {
        SimpleEntry(date: Date(), state: getOpeningCountDown.invoke())
    }
    
    func getSnapshot(in context: Context, completion: @escaping (SimpleEntry) -> ()) {
        let entry = SimpleEntry(date: Date(), state: getOpeningCountDown.invoke())
        completion(entry)
    }
    
    func getTimeline(in context: Context, completion: @escaping (Timeline<Entry>) -> ()) {
        var entries: [SimpleEntry] = []
        let currentDate = Date()
        // Update every 30 min for 24h
        for halfHourOffset in 0 ..< 48 {
            let entryDate = Calendar.current.date(byAdding: .minute, value: halfHourOffset * 30, to: currentDate)!
            let countdown = getOpeningCountDown.invoke(minuteOffset: Int32(halfHourOffset * 30))
            let entry = SimpleEntry(date: entryDate, state: countdown)
            entries.append(entry)
        }
        let timeline = Timeline(entries: entries, policy: .atEnd)
        completion(timeline)
    }
}

struct SimpleEntry: TimelineEntry {
    let date: Date
    let state: CountDown
}

struct CountdownWidgetEntryView : View {
    var entry: Provider.Entry
    @Environment(\.widgetFamily) var family
    
    var body: some View {
        GeometryReader { metrics in
            switch family {
            case .systemSmall: SmallWidget(metrics: metrics, entry: entry)
            case .systemMedium: MediumWidget(metrics: metrics, entry: entry)
            default: Text("Invalid Widget size").padding()
            }
        }
    }
}

struct CountdownWidget: Widget {
    let kind: String = "CountdownWidget"
    
    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: Provider()) { entry in
            if #available(iOS 17.0, *) {
                CountdownWidgetEntryView(entry: entry)
                    .containerBackground(for: .widget, content: {
                        Background(entry: entry)
                    })
            } else {
                CountdownWidgetEntryView(entry: entry)
                    .background {
                        Background(entry: entry)
                    }
            }
        }
        .configurationDisplayName(Text(Res.strings().widget_countdown_name.desc().localized()))
        .description(Text(Res.strings().widget_countdown_description.desc().localized()))
        .supportedFamilies([.systemSmall, .systemMedium])
        .contentMarginsDisabled()
    }
}

struct Background: View {
    @Environment(\.widgetFamily) var family
    
    var entry: Provider.Entry
    
    var body: some View {
        GeometryReader { metrics in
            let alignment = entry.state is CountDownInFuture ? Alignment.bottomLeading : Alignment.center
            ZStack(alignment: alignment) {
                VStack(spacing: 0) {
                    Color("StoppelSky")
                    Color("StoppelField").frame(height: metrics.size.height * 0.25)
                }
                if entry.state is CountDownInFuture {
                    if(family == .systemSmall) {
                        ZStack {
                            Image(uiImage: Res.images.shared.jan_libett_corner.toUIImage()!)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                        }
                    }
                }
                if entry.state is CountDownOnGoing {
                    let image = if (family == .systemSmall) { 
                        Res.images.shared.background_rides.toUIImage()!
                    } else { 
                        Res.images.shared.background_rides_wide.toUIImage()!
                    }
                    Image(uiImage: image)
                }
            }
        }
    }
}

struct SmallWidget: View {
    var metrics: GeometryProxy
    var entry: SimpleEntry
    var body: some View {
        VStack(spacing: 0) {
            if let inFuture = entry.state as? CountDownInFuture {
                VStack(alignment: /*@START_MENU_TOKEN@*/.center/*@END_MENU_TOKEN@*/, spacing: 0) {
                    Spacer()
                    Text(Res.strings().widget_countdown_prefix.desc().localized())
                        .font(Font.custom("RobotoSlab-Regular", fixedSize: 12))
                        .foregroundStyle(.black)
                    Text(inFuture.daysLeft.description)
                        .font(Font.custom("RobotoSlab-Light", fixedSize: 48))
                        .foregroundStyle(.black)
                        .padding(.vertical, -8)
                    Text(Res.plurals().countdownCard_unit_day.desc(number: inFuture.daysLeft).localized())
                        .font(Font.custom("RobotoSlab-Regular", fixedSize: 12))
                        .foregroundStyle(.black)
                    Spacer()
                }
                .padding(.leading, 48)
                .frame(height: metrics.size.height * 0.75)
                
                ZStack(alignment: .center) {
                    Text(Res.strings().widget_countdown_footer_multiline.format(args: [entry.state.season.year]).localized())
                        .font(Font.custom("RobotoSlab-Regular", fixedSize: 12))
                        .multilineTextAlignment(.center)
                        .foregroundStyle(.black)
                }
                .padding(.leading, 48)
                .frame(height: metrics.size.height * 0.25)
            } else {
                OnGoingView(metrics: metrics, entry: entry)
            }
        }
        .frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/)
    }
}

struct MediumWidget: View {
    var metrics: GeometryProxy
    var entry: SimpleEntry
    var body: some View {
        HStack {
            if let inFuture = entry.state as? CountDownInFuture {
                Spacer()
                Image(uiImage: Res.images.shared.jan_libett_full.toUIImage()!)
                Spacer()
                VStack(spacing: 0) {
                    VStack(alignment: /*@START_MENU_TOKEN@*/.center/*@END_MENU_TOKEN@*/, spacing: 0) {
                        Spacer()
                        Text(Res.strings().widget_countdown_prefix.desc().localized())
                            .font(Font.custom("RobotoSlab-Regular", fixedSize: 12))
                            .foregroundStyle(.black)
                        HStack {
                            VStack(alignment: /*@START_MENU_TOKEN@*/.center/*@END_MENU_TOKEN@*/) {
                                Text(inFuture.daysLeft.description)
                                    .font(Font.custom("RobotoSlab-Light", fixedSize: 48))
                                    .foregroundStyle(.black)
                                    .padding(.vertical, -8)
                                Text(Res.plurals().countdownCard_unit_day.desc(number: inFuture.daysLeft).localized())
                                    .font(Font.custom("RobotoSlab-Regular", fixedSize: 12))
                                    .foregroundStyle(.black)
                            }
                            .padding(.horizontal, 4)
                            
                            VStack(alignment: /*@START_MENU_TOKEN@*/.center/*@END_MENU_TOKEN@*/) {
                                Text(inFuture.hoursLeft.description)
                                    .font(Font.custom("RobotoSlab-Light", fixedSize: 48))
                                    .foregroundStyle(.black)
                                    .padding(.vertical, -8)
                                Text(Res.plurals().countdownCard_unit_hour.desc(number: inFuture.hoursLeft).localized())
                                    .font(Font.custom("RobotoSlab-Regular", fixedSize: 12))
                                    .foregroundStyle(.black)
                            }
                            .padding(.horizontal, 4)
                        }
                        Spacer()
                    }
                    .frame(height: metrics.size.height * 0.75)
                    
                    ZStack(alignment: .center) {
                        Text(Res.strings().widget_countdown_footer.format(args: [entry.state.season.year]).localized())
                            .font(Font.custom("RobotoSlab-Regular", fixedSize: 16))
                            .foregroundStyle(.black)
                    }
                    .frame(height: metrics.size.height * 0.25)
                }
                Spacer()
            } else {
                OnGoingView(metrics: metrics, entry: entry)
            }
        }
        .frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/)
    }
}

struct OnGoingView: View {
    var metrics: GeometryProxy
    var entry: SimpleEntry
    
    var body: some View {
        VStack(alignment: /*@START_MENU_TOKEN@*/.center/*@END_MENU_TOKEN@*/, spacing: 0) {
            Text(Res.strings().widget_countdown_ongoing_title.desc().localized())
                .font(Font.custom("RobotoSlab-Regular", fixedSize: 12))
                .foregroundStyle(.black)
                .padding(.top, 8)
            Spacer()
                .background {
                    Image(uiImage: Res.images.shared.jan_libett_full.toUIImage()!)
                        .resizable()
                        .scaledToFill()
                        .frame(height: metrics.size.height * 0.7)
                }
            Text(Res.strings().widget_countdown_footer.format(args: [entry.state.season.year]).localized())
                .font(Font.custom("RobotoSlab-Regular", fixedSize: 12))
                .foregroundStyle(.black)
                .padding(.bottom, 8)
            
        }
    }
}
