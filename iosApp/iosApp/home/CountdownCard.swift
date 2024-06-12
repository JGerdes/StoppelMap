import SwiftUI
import Shared

struct CountdownCard: View {
    var days: Int32
    var hours: Int32
    var minutes: Int32
    var seconds: Int32
    var year: Int32
    
    var body: some View {
        VStack(alignment: .leading) {
            Spacer()
                .frame(height: 8.0)
            Grid() {
                GridRow {
                    CountdownUnit(
                        value: days.formatted(),
                        unitLabel: Res.plurals().countdownCard_unit_day.desc(number: hours).localized()
                    )
                    CountdownUnit(
                        value: hours.formatted(),
                        unitLabel: Res.plurals().countdownCard_unit_hour.desc(number: hours).localized()
                    )
                }
                GridRow {
                    CountdownUnit(
                        value: minutes.formatted(),
                        unitLabel: Res.plurals().countdownCard_unit_minute.desc(number: minutes).localized()
                    )
                    CountdownUnit(
                        value: seconds.formatted(),
                        unitLabel: Res.plurals().countdownCard_unit_second.desc(number: seconds).localized()
                    )
                }
            }
            Spacer()
                .frame(height: 8.0)
            HStack{
                Spacer()
                Text(String(
                    format: Res.strings().countdownCard_suffix.desc().localized(),
                    arguments: [year]
                ))
                    .foregroundColor(.white)
            }
            
        }
        .padding()
        .background(.accent)
        .cornerRadius(24.0)
        .padding()
    }
}

struct CountdownUnit: View {
    var value: String
    var unitLabel: String
    
    var body: some View {
        VStack{
            Text(value).font(.largeTitle)
            Text(unitLabel)
        }.frame(maxWidth: .infinity)
        .padding()
        .background(.background)
        .cornerRadius(8.0)
    }
}

#Preview {
    CountdownCard(
        days: 1,
        hours: 999,
        minutes: 0,
        seconds: 0,
        year: 2024
    )
}
