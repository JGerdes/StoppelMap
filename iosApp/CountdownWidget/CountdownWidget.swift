import WidgetKit
import SwiftUI
import Shared

struct Provider: TimelineProvider {
    
    let getOpeningCountDown = CountdownDependencies().getOpeningCountDownUseCase
    
    func placeholder(in context: Context) -> SimpleEntry {
        SimpleEntry(date: Date(), state: CountDownInFuture(daysLeft: 0, hoursLeft: 0, minutesLeft: 0, secondsLeft: 0, year: 0))
    }

    func getSnapshot(in context: Context, completion: @escaping (SimpleEntry) -> ()) {
        let entry = SimpleEntry(date: Date(), state: CountDownInFuture(daysLeft: 12, hoursLeft: 4, minutesLeft: 0, secondsLeft: 0, year: 2024))
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

    var body: some View {
        VStack {
            if let inFuture = entry.state as? CountDownInFuture {
                HStack {
                    VStack {
                        Text(inFuture.daysLeft.description)
                        Text(Res.plurals().countdownCard_unit_day.desc(number: inFuture.daysLeft).localized())
                    }
                    VStack {
                        Text(inFuture.hoursLeft.description)
                        Text(Res.plurals().countdownCard_unit_hour.desc(number: inFuture.hoursLeft).localized())
                    }
                }
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
                    .containerBackground(.fill.tertiary, for: .widget)
            } else {
                CountdownWidgetEntryView(entry: entry)
                    .padding()
                    .background()
            }
        }
        .configurationDisplayName("Countdown Widget")
        .description("Zeigt an, wie viele Tage es noch bis zum Stoppelmarkt sind.")
    }
}

