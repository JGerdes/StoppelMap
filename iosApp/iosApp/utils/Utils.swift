import Foundation
import SwiftUI

protocol ScopeFunc {}
extension ScopeFunc {
    @inline(__always) func apply(block: (Self) -> ()) -> Self {
        block(self)
        return self
    }
    @inline(__always) func with<R>(block: (Self) -> R) -> R {
        return block(self)
    }
}

extension NSObject: ScopeFunc {}


extension View {
    @ViewBuilder func `if`<Content: View>(_ condition: Bool, transform: (Self) -> Content) -> some View {
        if condition {
            transform(self)
        } else {
            self
        }
    }
}

extension Dictionary where Key: ExpressibleByStringLiteral, Value: ExpressibleByStringLiteral {
    func forCurrentLanguage() -> Value {
        if let currentLanguageCode = Locale.current.language.languageCode?.identifier {
            return self[currentLanguageCode as! Key] ?? self.first!.value
        } else {
            return self.first!.value
        }
    }
}


extension Color {
    func mix(with color: Color, by percentage: Double) -> Color {
        let clampedPercentage = min(max(percentage, 0), 1)
        
        let components1 = UIColor(self).cgColor.components!
        let components2 = UIColor(color).cgColor.components!
        
        let red = (1.0 - clampedPercentage) * components1[0] + clampedPercentage * components2[0]
        let green = (1.0 - clampedPercentage) * components1[1] + clampedPercentage * components2[1]
        let blue = (1.0 - clampedPercentage) * components1[2] + clampedPercentage * components2[2]
        let alpha = (1.0 - clampedPercentage) * components1[3] + clampedPercentage * components2[3]
        
        return Color(red: red, green: green, blue: blue, opacity: alpha)
    }
}

