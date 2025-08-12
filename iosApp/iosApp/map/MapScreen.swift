import Shared
import SwiftUI

struct MapScreen: View {
    @Environment(\.colorScheme) var colorScheme

    let viewModel = MapDependencies().with {
        MapViewModel(
            deeplinkRepository: $0.deeplinkRepository,
            getMapFilePath: $0.getMapFilePathUseCase,
            searchMap: $0.searchMapUseCase,
            mapEntityRepository: $0.mapEntityRepository,
            locationRepository: $0.locationRepository,
            eventRepository: $0.eventRepository,
            permissionRepository: $0.permissionRepository,
            getQuickSearchItems: $0.getQuickSearchSuggestionsUseCase
        )
    }

    var onDismissSearch: () -> Void

    @State
    var viewState: MapViewModel.ViewState = MapViewModel.ViewState()

    @State
    private var showSheet = false

    @State
    private var searchQuery = ""

    @State
    private var isSearchActive = false

    var body: some View {
        NavigationStack {
            ZStack(alignment: .topTrailing) {
                if let mapDataPath = viewState.mapDataPath {
                    MapView(
                        mapDataPath: mapDataPath,
                        mapState: viewState.mapState,
                        colorScheme: colorScheme,
                        onMapTap: { viewModel.onMapTap(entitySlug: $0) },
                        onCameraDispatched: {
                            viewModel.onCameraUpdateDispatched()
                        },
                        onCameraMoved: { viewModel.onCameraMoved() },
                        permissionState: viewState.locationState.permissionState
                    )
                    .ignoresSafeArea()
                }
                if isSearchActive && !viewState.searchState.results.isEmpty {
                    List {
                        ForEach(viewState.searchState.results, id: \.term) {
                            result in
                            HStack(spacing: 16) {
                                if let icon = result.icon {
                                    Image(icon.id).resizable().frame(
                                        width: 24,
                                        height: 24
                                    )
                                } else {
                                    Color.clear.frame(width: 24, height: 24)
                                }
                                if let supportingText = result.supportingText()
                                {
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
                                viewModel.onSearchResultTap(
                                    searchResult: result
                                )
                            }
                        }
                    }
                    .scrollContentBackground(.hidden)
                    .background(.ultraThinMaterial)
                }

                VStack(alignment: .trailing) {
                    if (viewState.bottomSheetState
                        is MapViewModelBottomSheetStateIdle
                        || viewState.bottomSheetState
                            is MapViewModelBottomSheetStateHidden)
                        && !viewState.searchState.quickSearchChips.isEmpty
                    {
                        ScrollView(.horizontal, showsIndicators: false) {
                            HStack(spacing: 8) {
                                ForEach(
                                    viewState.searchState.quickSearchChips,
                                    id: \.term
                                ) { suggestion in
                                    HStack {
                                        if let image = suggestion.icon?.id {
                                            Image(image).resizable().frame(
                                                width: 18,
                                                height: 18
                                            )
                                        }
                                        Text(suggestion.term).foregroundStyle(
                                            .primary
                                        )
                                    }
                                    .padding(.horizontal, 8)
                                    .padding(.vertical, 4)
                                    .background(.thickMaterial)
                                    .clipShape(.capsule)
                                    .shadow(radius: 4)
                                    .padding(.top)
                                    .padding(.bottom, 5)
                                    .onTapGesture {
                                        viewModel.onSearchResultTap(
                                            searchResult: suggestion
                                        )
                                    }

                                }
                            }.padding(.horizontal)
                        }.frame(maxWidth: .infinity)
                    }

                    HStack {
                        Spacer()
                        Button(
                            action: {
                                if viewState.locationState.permissionState
                                    == PermissionState.notDetermined
                                {
                                    viewModel.requestLocationPermission()
                                } else {
                                    viewModel.onLocationButtonTap()
                                }
                            },
                            label: {
                                ZStack {
                                    Image(
                                        systemName: viewState.locationState
                                            .isFollowingLocation
                                            ? "location.fill" : "location"
                                    )
                                }
                                .foregroundColor(
                                    viewState.locationState.isFollowingLocation
                                        ? .accent : .primary
                                )
                                .frame(width: 40, height: 40)
                                .background(.thickMaterial)
                                .cornerRadius(8.0)
                                .shadow(radius: 8.0)
                            }
                        )
                        .padding(.horizontal)
                        .padding(.top, 5)
                    }.transition(.move(edge: .top)).animation(.default)
                }.frame(maxWidth: .infinity)

                if viewState.locationState.showNotInAreaHint {
                    HStack {
                        Text(
                            Res.strings().map_location_not_in_area.desc()
                                .localized()
                        )
                    }
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(.regularMaterial)
                    .cornerRadius(8.0)
                    .padding()
                    .environment(
                        \.colorScheme,
                        (colorScheme == .dark) ? .light : .dark
                    )
                    .task {
                        try? await Task.sleep(nanoseconds: 4_000_000_000)
                        viewModel.onNotInAreaHintShow()
                    }
                }

            }.apply {
                if #available(iOS 17.0, *) {
                    $0.searchable(
                        text: $searchQuery,
                        isPresented: $isSearchActive,
                        prompt: Res.strings().map_search_placeholder.desc()
                            .localized()
                    )
                } else {
                    $0.searchable(
                        text: $searchQuery,
                        prompt: Res.strings().map_search_placeholder.desc()
                            .localized()
                    )
                }
            }
            .toolbarBackground(.visible, for: .navigationBar)
            .toolbarBackground(.ultraThinMaterial, for: .navigationBar)
            .sheet(
                isPresented: $showSheet,
                onDismiss: { viewModel.onBottomSheetClose() },
                content: {
                    ScrollView {
                        ZStack(alignment: .topLeading) {
                            if let singleStall = viewState.bottomSheetState as? MapViewModelBottomSheetStateSingleStallLoaded {
                                let entity = singleStall.fullMapEntity
                                SingleStallBottomSheetContent(entity: entity)
                            } else if let collection = viewState.bottomSheetState as? MapViewModelBottomSheetStateCollection {
                                VStack(alignment: .leading, spacing: 8) {
                                    Text(collection.name).font(.title)
                                    Text(collection.subline()).font(.caption)
                                }
                                .frame(
                                    maxWidth: .infinity,
                                    alignment: .topLeading
                                )
                            }
                        }
                        .padding()
                    }
                    .frame(
                        maxWidth: .infinity,
                        maxHeight: .infinity,
                        alignment: .topLeading
                    )
                    .presentationDetents([.fraction(0.2), .medium, .large])
                    .presentationDragIndicator(.visible)
                    .apply {
                        if #available(iOS 16.4, *) {
                            $0
                                .presentationBackgroundInteraction(.enabled)
                                .presentationBackground(.thinMaterial)
                        } else {
                            $0
                        }
                    }
                }
            )
        }
        .onChange(of: searchQuery) { query in
            viewModel.onSearch(query: query)
        }
        .task {
            for await state in viewModel.state {
                viewState = state
                showSheet =
                    !(viewState.bottomSheetState
                    is MapViewModelBottomSheetStateHidden)
            }
        }
    }
}

struct SingleStallBottomSheetContent: View {
    
    let entity: FullMapEntity
    
    init(entity: FullMapEntity) {
        self.entity = entity
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            HStack(alignment: .center) {
                Text(entity.name).font(.title)
                Spacer()
                ShareLink(
                    item: Res.strings()
                        .map_sheet_share_text.format(
                            args: [
                                entity.slug
                            ]).localized()
                ).labelStyle(.iconOnly)
            }
            if let subline = entity.subline()
            {
                Text(subline).font(.caption)
            }
            if entity.images.count > 0 {
                TabView {
                    ForEach(entity.images, id: \.url) { image in
                        ZStack(alignment: .bottomLeading) {
                            if let url = URL(string: image.url) {
                                Color.clear.overlay(
                                    CachedAsyncImage(url: url) {image in
                                        image
                                            .resizable()
                                            .scaledToFill()
                                    } placeholder: {
                                        if let placeholder = UIImage(blurHash: image.blurHash, size: CGSize(width: 40, height: 30)) {
                                            Image(uiImage: placeholder)
                                                .resizable()
                                                .scaledToFill()
                                        }
                                    }
                                ).clipped()
                            }
                        }
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                    }
                }
                .frame(maxWidth: .infinity, minHeight: 192, maxHeight: 256)
                .tabViewStyle(PageTabViewStyle())
                .cornerRadius(8)
            }
            if let description = entity.description_
            {
                Text(description).fixedSize(
                    horizontal: false,
                    vertical: true
                )
            }
            if entity.admissionFees.count > 0 {
                FeesCard(state: entity)
            }
            if entity.events.count > 0 {
                VStack(alignment: .leading, spacing: 8) {
                    Text(Res.strings().map_sheet_events_title.desc().localized()).font(.headlineSlab)
                    ForEach(
                        entity.events.indices, id: \.self
                    ) { dayIndex in
                        let day = entity.events[dayIndex]
                        Text(day.date.dayOfWeek.toFullResourceString().desc().localized())
                        ForEach(
                            day.events.indices, id: \.self
                        ) { eventIndex in
                            let event = day.events[eventIndex]
                            HStack {
                                ZStack {
                                    Text("00:00")
                                        .hidden()
                                    Text(event.start.time.description())
                                }
                                Text(event.name.localized())
                            }.padding(.horizontal)
                        }
                    }
                }
            }
            if entity.websites.count > 0 {
                VStack(alignment: .leading, spacing: 8) {
                    Text(Res.strings().map_sheet_websites_title.desc().localized())
                    ForEach(
                        entity.websites.indices, id: \.self
                    ) { index in
                        Link(destination: URL(string: entity.websites[index].url)!) {
                            Label(entity.websites[index].url, systemImage: "link")
                        }
                    }
                }
            }
        }
    }
}



struct FeesCard: View {
    
    let state: FullMapEntity
    let numberFormatter = NumberFormatter()
    
    init(state: FullMapEntity) {
        self.state = state
        numberFormatter.numberStyle = .decimal
        numberFormatter.maximumFractionDigits = 2
        numberFormatter.minimumFractionDigits = 2
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(Res.strings().map_sheet_prices_title.desc().localized()).font(.caption)
            
            
            ForEach(state.admissionFees, id: \.self.name) { fee in
                HStack {
                    Text(fee.name)
                    Spacer()
                    if let priceText = numberFormatter.string(from: NSNumber(value: Double(fee.price) / 100.0)) {
                        Text(priceText + " €")
                    }
                }.frame(maxWidth: .infinity)
            }
        }
        .padding()
        .background(.thinMaterial)
        .cornerRadius(10)
    }
}
