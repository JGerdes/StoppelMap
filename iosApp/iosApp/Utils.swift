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

