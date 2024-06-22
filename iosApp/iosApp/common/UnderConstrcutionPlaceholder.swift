import SwiftUI
import Shared

struct UnderConstrcutionPlaceholder: View {
    var body: some View {
        VStack(){
            Text(Res.strings().common_available_soon.desc().localized())
                .font(.title)
        }.frame(maxWidth: .infinity, maxHeight: .infinity)
            .ignoresSafeArea()
            .background(.ultraThinMaterial)
    }
}

#Preview {
    UnderConstrcutionPlaceholder()
}
