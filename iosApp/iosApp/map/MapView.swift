import SwiftUI
import MapLibre
import Shared


struct MapView: UIViewRepresentable {
    typealias UIViewType = MLNMapView

    var mapDataPath: OkioPath
    var mapState: MapState
    var colorScheme: ColorScheme
    var onMapTap: (String?) -> ()
    var onCameraDispatched: () -> ()
    
    func makeUIView(context: Context) -> MLNMapView {
        
        // Create the map view
        let mapView = MLNMapView(frame: .zero, styleURL: URL(string:"asset://map/style.json"))
        mapView.delegate = context.coordinator
        
        mapView.logoView.isHidden = true
        mapView.attributionButton.isHidden = true
        mapView.allowsTilting = false
        mapView.minimumZoomLevel = MapDefaults.shared.minZoom
        mapView.maximumZoomLevel = MapDefaults.shared.maxZoom
        mapView.visibleCoordinateBounds = MLNCoordinateBounds(
            sw: CLLocationCoordinate2D(
                    latitude: MapDefaults.shared.cameraBounds.southLat,
                    longitude: MapDefaults.shared.cameraBounds.westLng
            ),
            ne: CLLocationCoordinate2D(
                latitude: MapDefaults.shared.cameraBounds.northLat,
                longitude: MapDefaults.shared.cameraBounds.eastLng
            )
        )
    
        
        mapView.setCenter(
            CLLocationCoordinate2D(
                latitude: MapDefaults.shared.center.lat,
                longitude: MapDefaults.shared.center.lng
            ),
            animated: false
        )
        
        let singleTap = UITapGestureRecognizer(
            target: context.coordinator,
            action: #selector(Coordinator.handleMapTap(sender:))
        )
        for recognizer in mapView.gestureRecognizers! where recognizer is UITapGestureRecognizer {
            singleTap.require(toFail: recognizer)
        }
        mapView.addGestureRecognizer(singleTap)
    
        
        mapView.setZoomLevel(16.2, animated: false)
        
        
        return mapView
        
    }
    
    func updateUIView(_ mapView: MLNMapView, context: Context) {
        context.coordinator.update(colorScheme: colorScheme, mapState: mapState)
    }
    
    func makeCoordinator() -> MapView.Coordinator {
        Coordinator(self)
    }
    
    final class Coordinator: NSObject, MLNMapViewDelegate {
        var control: MapView
        var mapView: MLNMapView? = nil
        
        init(_ control: MapView) {
            self.control = control
        }
        
        func update(colorScheme: ColorScheme, mapState: MapState) {
            mapView?.updateLayerColors(colorScheme: colorScheme)
            mapView?.updateWithState(mapState: mapState, onCameraDispatched: control.onCameraDispatched)
        }
        
        func mapViewDidFinishLoadingMap(_ mapView: MLNMapView) {
            self.mapView = mapView
            let mapUrl = URL(string: "file://" + self.control.mapDataPath.description())!
            print("mapView, mapViewDidFinishLoadingMap. mapUrl:" + mapUrl.absoluteString)
            let source = MLNShapeSource(identifier: "composite", url: mapUrl)
            mapView.style!.addSource(source)
            mapView.updateLayerColors(colorScheme: self.control.colorScheme)
            
        }
        
        func mapViewDidFailLoadingMap(_ mapView: MLNMapView, withError error: any Error) {
            print("mapView, failed loading: " + error.localizedDescription)
        }
        
        @objc func handleMapTap(sender: UITapGestureRecognizer) {
            guard let mapView = sender.view as? MLNMapView else { return }


            // Convert tap location (CGPoint) to geographic coordinate (CLLocationCoordinate2D).
            let tapPoint: CGPoint = sender.location(in: mapView)
            let tapCoordinate: CLLocationCoordinate2D = mapView.convert(tapPoint, toCoordinateFrom: nil)
            print("You tapped at: \(tapCoordinate.latitude), \(tapCoordinate.longitude)")

            let results = mapView.visibleFeatures(at: tapPoint, styleLayerIdentifiers: [
               "labels",
               "rides",
               "bars",
               "restaurants",
               "restrooms",
               "miscs",
               "food-stalls",
               "candy-stalls",
               "game-stalls",
               "seller-stalls",
               "expo-stalls"
            ])
            
            let slug = results.first?.attributes["slug"] as? String
            control.onMapTap(slug)
        
        }
    }
}



extension MLNMapView {
    func updateWithState(mapState: MapState, onCameraDispatched: () -> ()) {
        print("mapView, updateWithState: "+mapState.description())
        
        if let symbolLayer = style!.layers.first(where: {$0.identifier == "labels"}) as? MLNSymbolStyleLayer {
            symbolLayer.isVisible = mapState.highlightedEntities?.isEmpty ?? true
        }
        
        if let symbolLayer = style!.layers.first(where: {$0.identifier == "highlight-labels"}) as? MLNSymbolStyleLayer {
            symbolLayer.isVisible = !(mapState.highlightedEntities?.isEmpty ?? true)
        }

        if let highlights = mapState.highlightedEntities {
            if(!highlights.isEmpty) {
                if let highlightSource = style!.source(withIdentifier: "geojson-marker") as? MLNShapeSource {
                    highlightSource.shape = MLNShapeCollectionFeature(shapes: highlights.map{ entity in
                        MLNPointFeature().apply{
                            $0.coordinate = CLLocationCoordinate2D(latitude: entity.location.lat, longitude: entity.location.lng)
                            $0.attributes["building"] = entity.icon.id
                            $0.attributes["name"] = entity.name
                        }
                    })
                }
            }
        }

        if let cameraView = mapState.camera as? CameraViewFocusLocation {
            self.setCenter(CLLocationCoordinate2D(latitude: cameraView.location.lat, longitude: cameraView.location.lng), animated: true)
            onCameraDispatched()
        } else if let cameraView = mapState.camera as? CameraViewBounding {
            self.setVisibleCoordinateBounds(
                MLNCoordinateBounds(
                    sw: CLLocationCoordinate2D(latitude: cameraView.bounds.southLat, longitude: cameraView.bounds.westLng),
                    ne: CLLocationCoordinate2D(latitude: cameraView.bounds.northLat, longitude: cameraView.bounds.eastLng)
                ),
                edgePadding: UIEdgeInsets(top: 192, left: 48, bottom: 128, right: 48),
                animated: true
            )
            onCameraDispatched()
        }
    }
    
    func updateLayerColors(colorScheme: ColorScheme) {
        let mapBackground = colorScheme == .dark ? mapBackgroundDark : mapBackgroundLight
        let mapColors = colorScheme == .dark ? mapColorsDark : mapColorsLight
        let mapSymbolBackgroundColors = colorScheme == .dark ? mapSymbolBackgroundDark : mapSymbolBackgroundLight
        let iconColor = colorScheme == .dark ? Color.black : Color.white
        
        if let backgroundLayer = style!.layers.first(where: {$0.identifier == "background"}) as? MLNBackgroundStyleLayer {
            backgroundLayer.backgroundColor = NSExpression(forConstantValue: mapBackground)
        }
        
        if let symbolLayer = style!.layers.first(where: {$0.identifier == "labels"}) as? MLNSymbolStyleLayer {
            symbolLayer.textColor = NSExpression(forConstantValue: colorScheme == .dark ? labelColorDark : labelColorLight)
            symbolLayer.textHaloColor = NSExpression(forConstantValue: colorScheme == .dark ? labelHaloColorDark : labelHaloColorLight)
        }
        
        if let symbolLayer = style!.layers.first(where: {$0.identifier == "highlight-labels"}) as? MLNSymbolStyleLayer {
            symbolLayer.textColor = NSExpression(forConstantValue: colorScheme == .dark ? labelColorDark : labelColorLight)
            symbolLayer.textHaloColor = NSExpression(forConstantValue: colorScheme == .dark ? labelHaloColorDark : labelHaloColorLight)
        }
        
        mapColors.forEach { (layerId: String, color: UIColor) in
            if let layer = style!.layers.first(where: {$0.identifier == layerId}) as? MLNFillStyleLayer {
                layer.fillColor = NSExpression(forConstantValue: color)
            }
        }
        
        style!.setImage(UIImage(systemName: "bus")!, forName: "station")
        style!.setImage(UIImage(systemName: "info")!, forName: "misc")
        
        let icons = [
            "bar",
            "candy_stall",
            "expo",
            "food_stall",
            "game_stall",
            "misc",
            "parking",
            "restaurant",
            "restroom",
            "ride",
            "seller_stall",
            "entrance",
            "station",
            "platform",
            "taxi"
        ];
        
        icons.forEach {id in
            
            let renderer = ImageRenderer(content: Image(id)
                .resizable()
                .renderingMode(.template)
                .foregroundColor(iconColor)
                .scaledToFill()
                .frame(width: 12, height: 12)
                .padding(4.0)
                .background(Color(uiColor: mapSymbolBackgroundColors[id] ?? mapSymbolBackgroundColors["misc"]!))
                .cornerRadius(24.0)
            )
            renderer.scale = UIScreen.main.scale
            
            style!.setImage(renderer.uiImage!, forName: id)
        }
        
        
    }
}
