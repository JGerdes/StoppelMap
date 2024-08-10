import SwiftUI
import MapLibre
import Shared


struct MapView: UIViewRepresentable {
    typealias UIViewType = MLNMapView

    var mapDataPath: OkioPath
    var colorScheme: ColorScheme
    
    func makeUIView(context: Context) -> MLNMapView {
        
        // Create the map view
        let mapView = MLNMapView(frame: .zero, styleURL: URL(string:"asset://map/style.json"))
        mapView.delegate = context.coordinator
        
        mapView.logoView.isHidden = true
        mapView.attributionButton.isHidden = true
        
        
        mapView.setCenter(
            CLLocationCoordinate2D(
                latitude: 52.7477, longitude: 8.2956
            ),
            animated: false
        )
        
        mapView.setZoomLevel(16.2, animated: false)
        return mapView
        
    }
    
    func updateUIView(_ mapView: MLNMapView, context: Context) {
        context.coordinator.update(colorScheme: colorScheme)
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
        
        func update(colorScheme: ColorScheme) {
            mapView?.updateLayerColors(colorScheme: colorScheme)
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
    }
}


extension MLNMapView {
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
