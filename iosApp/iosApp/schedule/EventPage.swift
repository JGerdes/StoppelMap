import SwiftUI
import Shared

struct EventPage: View {
    var scheduleDay: ScheduleDay
    var selectedEvent: DataEvent?
    var onEventTap: (DataEvent) -> ()
    var onNotificationToggle: (_ event: DataEvent, _ isActive: Bool) -> ()
    
    var body: some View {
        ScrollView(.vertical) {
            VStack(spacing: 0) {
                ForEach(0..<scheduleDay.timeSlots.count, id: \.self) { timeSlotIndex in
                    let slot = scheduleDay.timeSlots[timeSlotIndex]
                    ForEach(0..<slot.events.count, id: \.self) { eventIndex in
                        let event = slot.events[eventIndex]
                        HStack(alignment: .top, spacing: 0) {
                            ZStack {
                                Text("00:00")
                                    .font(.title3)
                                    .padding()
                                    .hidden()
                                if(eventIndex == 0) {
                                    Text(slot.time.description())
                                        .font(.title3)
                                        .padding()
                                }
                            }
                            VStack {
                                EventRow(
                                    event: event,
                                    selected: event.event == selectedEvent,
                                    onSelect: {
                                        onEventTap(event.event)
                                    },
                                    onNotificationToggle: { active in
                                        onNotificationToggle(event.event, active)
                                    }
                                )
                                Divider()
                            }
                            
                        }.frame(
                            minWidth: 0,
                            maxWidth: .infinity,
                            alignment: .topLeading
                        )
                    }
                }
            }
        }
    }
}

struct EventRow: View {
    
    var event: ScheduleEvent
    var selected: Bool
    var onSelect: () -> ()
    var onNotificationToggle: (Bool) -> ()
    
    var body: some View {
        HStack(alignment: .top) {
            VStack(alignment: .leading) {
                if let location = event.event.location {
                    Text(location).font(.subheadline)
                }
                Text(event.event.name).font(.headline)
                if let description_ = event.event.description_ {
                    Text(description_)
                        .lineLimit(selected ? nil : 1)
                        .font(.footnote)
                }
            }.frame(
                minWidth: 0,
                maxWidth: .infinity,
                alignment: .topLeading
            )
                Button(
                    action: { onNotificationToggle(!event.bookmarked) }
                ){
                    Image(systemName: event.bookmarked ? "bookmark.fill" : "bookmark")
                }
                .opacity((selected || event.bookmarked) ? 1 : 0)
        }
        .padding()
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            alignment: .topLeading
        )
        
        .onTapGesture {
            onSelect()
        }
        .background(selected ? .accent.opacity(0.2) : .clear)
        .cornerRadius(8.0)
        .padding(.trailing)
        .animation(.easeInOut, value: selected)
    }
    
}
