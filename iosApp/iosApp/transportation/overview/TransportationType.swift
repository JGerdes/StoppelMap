import Foundation
import Shared

enum TransportationType: CaseIterable {
    case bus
    case taxi

    var titleStringDesc: ResourceStringDesc {
        switch self {
        case .bus:
            Res.strings().transportation_overview_section_bus.desc()
        case .taxi:
            Res.strings().transportation_overview_section_taxi.desc()
        }
    }
    
    var systemImage: String {
        switch self {
        case .bus:
            "bus.fill"
        case .taxi:
            "car"
        }
    }
}
