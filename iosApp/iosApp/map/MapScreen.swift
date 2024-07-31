import SwiftUI
import Shared

struct MapScreen: View {
    @Environment(\.colorScheme) var colorScheme
    
    let viewModel = MapDependencies().with {
        MapViewModel(getMapFilePath: $0.getMapFilePathUseCase)
    }
    
    @State
    var viewState: MapViewModel.ViewState = MapViewModel.ViewState()
    
    var body: some View {
        ZStack {
            if let mapDataPath = viewState.mapDataPath {
                MapView(mapDataPath: mapDataPath, colorScheme: colorScheme)
                    .ignoresSafeArea()
            }
                
        }.task {
            for await state in viewModel.state {
                viewState = state
            }
        }
    }
}

#Preview {
    MapScreen()
}
