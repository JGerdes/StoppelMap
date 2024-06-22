import SwiftUI
import Shared

struct CountdownCard: View {
    var days: Int32
    var hours: Int32
    var minutes: Int32
    var seconds: Int32
    var season: Season
    
    var body: some View {
        ZStack(alignment: .leading) {
            VStack(alignment: .trailing, spacing: 0){
                ZStack(alignment: .trailing) {
                    ZStack(alignment: .center) {
                        Grid() {
                            GridRow {
                                CountdownUnit(
                                    value: days,
                                    unitLabel: Res.plurals().countdownCard_unit_day,
                                    valueAnimated: Int(days)
                                )
                                CountdownUnit(
                                    value: hours,
                                    unitLabel: Res.plurals().countdownCard_unit_hour,
                                    valueAnimated: Int(hours)
                                )
                            }
                            GridRow {
                                CountdownUnit(
                                    value: minutes,
                                    unitLabel: Res.plurals().countdownCard_unit_minute,
                                    valueAnimated: Int(minutes)
                                )
                                CountdownUnit(
                                    value: seconds,
                                    unitLabel: Res.plurals().countdownCard_unit_second,
                                    valueAnimated: Int(seconds)
                                )
                            }
                        }
                        .padding([.vertical, .trailing])
                    }
                    .frame(maxWidth: .infinity, alignment: .center)
                    .padding(.leading, 128)
                }
                .frame(maxWidth: .infinity, alignment: .trailing)
                .background(Color("StoppelSky"))
                ZStack(alignment: .trailing) {
                    VStack(alignment: .trailing) {
                        Text(Res.strings().countdownCard_suffix.desc().localized())
                            .font(.bodySlab)
                        Text(Res.strings().countdownCard_iteration.format(args: [season.iteration]).localized())
                            .font(.titleSlab)
                        Text(Res.strings().countdownCard_dates.format(args: [
                            season.start.date.dayOfMonthFormat().localized(),
                            season.end.date.defaultFormat().localized()
                        ]).localized())
                            .font(.bodySlab)
                    }
                    .padding([.vertical, .trailing])
                    .padding(.leading, 128)
                }
                .frame(maxWidth: .infinity, alignment: .trailing)
                .background(Color("StoppelField"))
            }
            Color.clear
                .frame(maxWidth: 128, maxHeight: .infinity)
                .overlay(
                    Image(uiImage: Res.images.shared.jan_libett_balloons.toUIImage()!).resizable().scaledToFit()
                        .padding(8)
                )
        }
        .environment(\.colorScheme, .light)
        .cornerRadius(24.0)
        .padding()
    }
}

struct CountdownUnit: View {
    var value: Int32
    var unitLabel: PluralsResource
    
    @State var valueAnimated: Int
    
    var body: some View {
        ZStack {
            VStack{
                Text(valueAnimated.description)
                    .font(.extraLargeTitleSlab)
                    .contentTransition(.numericText())
                Text(unitLabel.desc(number: Int32(value)).localized())
                    .font(.bodySlab)
            }
            VStack {
                Text("99")
                    .font(.extraLargeTitleSlab)
                Text(unitLabel.desc(number: 99).localized())
                    .font(.bodySlab)
            }.hidden()
        }
        .onChange(of: value, perform: {v in withAnimation{
            valueAnimated = Int(v)
        }})
        .padding(4)
    }
}
