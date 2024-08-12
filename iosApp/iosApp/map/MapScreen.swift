import SwiftUI
import Shared

struct MapScreen: View {
    @Environment(\.colorScheme) var colorScheme
    
    let viewModel = MapDependencies().with {
        MapViewModel(
            getMapFilePath: $0.getMapFilePathUseCase,
            searchMap: $0.searchMapUseCase,
            mapEntityRepository: $0.mapEntityRepository
        )
    }
    
    var onDismissSearch: () -> ()
    
    @State
    var viewState: MapViewModel.ViewState = MapViewModel.ViewState()
    
    @State
    private var showSheet = false
    
    @State
    private var searchQuery = ""
    
    @State
    private var isSearchActive = false
    
    @State
    var bottomSheetContentHeight = CGFloat(0)
    
    var body: some View {
        NavigationStack {
            ZStack {
                if let mapDataPath = viewState.mapDataPath {
                    MapView(mapDataPath: mapDataPath,
                            mapState: viewState.mapState,
                            colorScheme: colorScheme,
                            onMapTap: {viewModel.onMapTap(entitySlug: $0)},
                            onCameraDispatched: {viewModel.onCameraUpdateDispatched()}
                    )
                    .ignoresSafeArea()
                }
                if(isSearchActive && !viewState.searchState.results.isEmpty) {
                    List {
                        ForEach(viewState.searchState.results, id: \.term) { result in
                            HStack(spacing: 16) {
                                if let icon = result.icon {
                                    Image(icon.id).resizable().frame(width: 24, height: 24)
                                } else {
                                    Color.clear.frame(width: 24, height: 24)
                                }
                                if let supportingText = result.supportingText() {
                                    VStack(alignment: .leading) {
                                        Text(result.term)
                                        Text(supportingText).font(.footnote)
                                    }
                                } else {
                                    Text(result.term)
                                }
                                Spacer()
                            }
                            .contentShape(Rectangle())
                            .onTapGesture {
                                onDismissSearch()
                                searchQuery = ""
                                isSearchActive = false
                                viewModel.onSearchResultTap(searchResult: result)
                            }
                        }
                    }
                    .scrollContentBackground(.hidden)
                    .background(.ultraThinMaterial)
                }
                
            }.apply{
                if #available(iOS 17.0, *) {
                    $0.searchable(text: $searchQuery, isPresented: $isSearchActive, prompt: Res.strings().map_search_placeholder.desc().localized())
                } else {
                    $0.searchable(text: $searchQuery, prompt: Res.strings().map_search_placeholder.desc().localized())
                }
            }
            .toolbarBackground(.visible, for: .navigationBar)
            .toolbarBackground(.ultraThinMaterial, for: .navigationBar)
            .sheet(isPresented: $showSheet, onDismiss: {viewModel.onBottomSheetClose()}, content: {
                ScrollView {
                    ZStack(alignment: .topLeading) {
                        if let singleStation = viewState.bottomSheetState as? MapViewModelBottomSheetStateSingleStall {
                            VStack(alignment: .leading, spacing: 8) {
                                Text(singleStation.fullMapEntity.name).font(.title)
                                if let subline = singleStation.fullMapEntity.subline() {
                                    Text(subline).font(.caption)
                                }
                                if let description = singleStation.fullMapEntity.description_ {
                                    Text(description).fixedSize(horizontal: false, vertical: true)
                                }
                            }
                            .frame(maxWidth: .infinity, alignment: .topLeading)
                        }
                    }
                    .padding()
                    .background {
                        //This is done in the background otherwise GeometryReader tends to expand to all the space given to it  like color or shape.
                        GeometryReader { proxy in
                            Color.clear
                                .onChange(of: proxy.size) { size in
                                    bottomSheetContentHeight = size.height
                                    print("updated bottomSheetContentHeight to "+bottomSheetContentHeight.description)
                                }
                        }
                    }
                }
                .presentationDetents([.height(min(128, bottomSheetContentHeight)), .height(bottomSheetContentHeight)])
                .presentationDragIndicator(.visible)
                .apply {
                    if #available(iOS 16.4, *) {
                        $0.presentationBackgroundInteraction(.enabled)
                    } else {
                        $0
                    }
                }
            })
        }
        .onChange(of: searchQuery) { query in
            viewModel.onSearch(query: query)
        }
        .task {
            for await state in viewModel.state {
                viewState = state
                showSheet = !(viewState.bottomSheetState is MapViewModelBottomSheetStateHidden)
            }
        }
    }
}

