import SwiftUI
import Shared
import Lottie

struct UnderConstrcutionPlaceholder: View {
    
    @Environment(\.colorScheme) var colorScheme: ColorScheme
    
    var body: some View {
        VStack(){
            let fillColor = if(colorScheme == .dark) {
                LottieColor(r: 1, g: 1, b: 1, a: 1)
            } else {
                LottieColor(r: 0, g: 0, b: 0, a: 1)
            }
            
            LottieView(animation: .named("UnderConstruction"))
                .looping()
                .valueProvider(ColorValueProvider(fillColor), for: AnimationKeypath(keypath: "**.Fill.Color"))
                .configure { lottie in
                    lottie.contentMode = .scaleAspectFit
                }
                .resizable()
                .frame(width: 256, height: 256)
                .opacity(0.8)
            Text(Res.strings().common_available_soon.desc().localized())
                .font(.titleSlab)
        }.frame(maxWidth: .infinity, maxHeight: .infinity)
            .ignoresSafeArea()
            .background(.ultraThinMaterial)
    }
}

#Preview {
    UnderConstrcutionPlaceholder()
}
