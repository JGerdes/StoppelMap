import SwiftUI
import Shared

struct MapScreen: View {
    @Environment(\.colorScheme) var colorScheme
    
    let viewModel = MapDependencies().with {
        MapViewModel(
            deeplinkRepository: $0.deeplinkRepository,
            getMapFilePath: $0.getMapFilePathUseCase,
            searchMap: $0.searchMapUseCase,
            mapEntityRepository: $0.mapEntityRepository,
            locationRepository: $0.locationRepository,
            permissionRepository: $0.permissionRepository,
            getQuickSearchItems: $0.getQuickSearchSuggestionsUseCase
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
            ZStack(alignment: .topTrailing) {
                if let mapDataPath = viewState.mapDataPath {
                    MapView(mapDataPath: mapDataPath,
                            mapState: viewState.mapState,
                            colorScheme: colorScheme,
                            onMapTap: {viewModel.onMapTap(entitySlug: $0)},
                            onCameraDispatched: {viewModel.onCameraUpdateDispatched()},
                            onCameraMoved: {viewModel.onCameraMoved()},
                            permissionState: viewState.locationState.permissionState
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
                
                VStack(alignment: .trailing) {
                    if((viewState.bottomSheetState is MapViewModelBottomSheetStateIdle || viewState.bottomSheetState is MapViewModelBottomSheetStateHidden) && !viewState.searchState.quickSearchChips.isEmpty) {
                        ScrollView(.horizontal, showsIndicators: false) {
                            HStack(spacing: 8) {
                                ForEach(viewState.searchState.quickSearchChips, id: \.term) { suggestion in
                                    HStack {
                                        if let image = suggestion.icon?.id {
                                            Image(image).resizable().frame(width: 18, height: 18)
                                        }
                                        Text(suggestion.term).foregroundStyle(.primary)
                                    }
                                    .padding(.horizontal, 8)
                                    .padding(.vertical, 4)
                                    .background(.thickMaterial)
                                    .clipShape(.capsule)
                                    .shadow(radius: 4)
                                    .padding(.top)
                                    .padding(.bottom, 5)
                                    .onTapGesture {
                                        viewModel.onSearchResultTap(searchResult: suggestion)
                                    }
                                    
                                }
                            }.padding(.horizontal)
                        }.frame(maxWidth: .infinity)
                    }
                    
                    HStack {
                        Spacer()
                        Button(action: {
                            if(viewState.locationState.permissionState == PermissionState.notDetermined) {
                                viewModel.requestLocationPermission()
                            } else {
                                viewModel.onLocationButtonTap()
                            }
                        }, label: {
                            ZStack {
                                Image(systemName: viewState.locationState.isFollowingLocation ? "location.fill" : "location")
                            }
                            .foregroundColor(viewState.locationState.isFollowingLocation ? .accent : .primary)
                            .frame(width: 40, height: 40)
                            .background(.thickMaterial)
                            .cornerRadius(8.0)
                            .shadow(radius: 8.0)
                        })
                        .padding(.horizontal)
                        .padding(.top, 5)
                    }.transition(.move(edge: .top)).animation(.default)
                }.frame(maxWidth: .infinity)
                
                if(viewState.locationState.showNotInAreaHint) {
                    HStack {
                        Text(Res.strings().map_location_not_in_area.desc().localized())
                    }
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(.regularMaterial)
                    .cornerRadius(8.0)
                    .padding()
                    .environment(\.colorScheme, (colorScheme == .dark) ? .light : .dark)
                    .task {
                        try? await Task.sleep(nanoseconds: 4_000_000_000)
                        viewModel.onNotInAreaHintShow()
                    }
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
                        } else if let collection = viewState.bottomSheetState as? MapViewModelBottomSheetStateCollection {
                            VStack(alignment: .leading, spacing: 8) {
                                Text(collection.name).font(.title)
                                Text(collection.subline()).font(.caption)
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

