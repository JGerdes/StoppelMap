import SwiftUI
import Shared

struct EventPage: View {
    var scheduleDay: ScheduleDay
    var selectedEvent: Event?
    var onEventTap: (Event) -> ()
    var onNotificationToggle: (_ event: Event, _ isActive: Bool) -> ()
    
    var body: some View {
        ScrollView(.vertical) {
            LazyVStack(spacing: 0, pinnedViews: [.sectionHeaders]) {
                ForEach(0..<scheduleDay.timeSlots.count, id: \.self) { timeSlotIndex in
                    let slot = scheduleDay.timeSlots[timeSlotIndex]
                    TimeSlot(
                        slot: slot,
                        selectedEvent: selectedEvent,
                        onEventTap: onEventTap,
                        onNotificationToggle: onNotificationToggle
                    )
                }
            }
        }
    }
}

struct SectionHeader: View {
    
    var slot: ScheduleTime
    
    var body: some View {
        ZStack {
            Text(slot.time.description())
                .font(.title3)
                .padding([.top, .horizontal])
        }
        .background(.background)
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            maxHeight: 1,
            alignment: .topLeading
        )
    }
}

struct TimeSlot: View {
    
    var slot: ScheduleTime
    var selectedEvent: Event?
    var onEventTap: (Event) -> ()
    var onNotificationToggle: (_ event: Event, _ isActive: Bool) -> ()
    
    var body: some View {
        Section(header: SectionHeader(slot: slot).listRowInsets(.none)) {
            ForEach(slot.events, id: \.slug) { event in
                HStack(alignment: .top, spacing: 0) {
                    ZStack {
                        Text("00:00")
                            .font(.title3)
                            .padding()
                            .hidden()
                        //                                    if(eventIndex == 0) {
                        //                                        Text(slot.time.description())
                        //                                            .font(.title3)
                        //                                            .padding()
                        //                                    }
                    }
                    VStack(spacing: 0) {
                        EventRow(
                            event: event,
                            selected: event == selectedEvent,
                            onSelect: {
                                onEventTap(event)
                            },
                            onNotificationToggle: { active in
                                onNotificationToggle(event, active)
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

struct EventRow: View {
    
    var event: Event
    var selected: Bool
    var onSelect: () -> ()
    var onNotificationToggle: (Bool) -> ()
    
    var body: some View {
        HStack(alignment: .top) {
            VStack(alignment: .leading) {
                if let location = event.locationName {
                    Text(location).font(.subheadline)
                }
                Text(event.name).font(.headline)
                if let description_ = event.description_ {
                    Text(description_.localized())
                        .lineLimit(selected ? nil : 1)
                        .font(.footnote)
                }
            }.frame(
                minWidth: 0,
                maxWidth: .infinity,
                alignment: .topLeading
            )
            Button(
                action: { onNotificationToggle(!event.isBookmarked) }
            ){
                Image(systemName: event.isBookmarked ? "bookmark.fill" : "bookmark")
            }
            .opacity((selected || event.isBookmarked) ? 1 : 0)
        }
        .padding(.horizontal)
        .padding(.vertical, selected ? 16 : 8)
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            alignment: .topLeading
        )
        .contentShape(Rectangle())
        .onTapGesture {
            onSelect()
        }
        .background(selected ? .accent.opacity(0.2) : .clear)
        .cornerRadius(8.0)
        .padding(.trailing)
        .padding(.vertical, 8)
        .animation(.spring(response: 0.3), value: selected)
    }
}
