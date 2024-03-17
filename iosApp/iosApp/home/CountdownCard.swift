import SwiftUI
import Shared

struct CountdownCard: View {
    var days: Int32
    var hours: Int32
    var minutes: Int32
    
    var body: some View {
        VStack(alignment: .leading) {
            Text("Nur noch")
                .foregroundColor(.white)
            Spacer()
                .frame(height: 8.0)
            HStack {
                CountdownUnit(value: days.formatted(), unitLabel: "Tage")
                CountdownUnit(value: hours.formatted(), unitLabel: "Stunden")
                CountdownUnit(value: minutes.formatted(), unitLabel: "Minuten")
            }
            Spacer()
                .frame(height: 8.0)
            HStack{
                Spacer()
                Text("bis zum Stoppelmark 2024")
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
        minutes: 0)
}
