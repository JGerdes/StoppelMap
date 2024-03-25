import SwiftUI
import MapLibre
import Shared

struct MapView: UIViewRepresentable {
    typealias UIViewType = MLNMapView
    
    
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
        // Update the view if needed
    }
    
    func makeCoordinator() -> MapView.Coordinator {
            Coordinator(self)
        }
        
        final class Coordinator: NSObject, MLNMapViewDelegate {
            var control: MapView
            
            init(_ control: MapView) {
                self.control = control
            }

            func mapViewDidFinishLoadingMap(_ mapView: MLNMapView) {
                print("mapView, mapViewDidFinishLoadingMap")
                let source = MLNShapeSource(identifier: "composite", url: Res.assets().mapdata.url)
                mapView.style!.addSource(source)
                if let ridesLayer = mapView.style!.layers.first(where: {$0.identifier == "rides"}) as? MLNFillStyleLayer {
                    ridesLayer.fillColor = NSExpression(forConstantValue: UIColor.red)
                }
            }
            
            func mapViewDidFailLoadingMap(_ mapView: MLNMapView, withError error: any Error) {
                print("mapView, failed loading: " + error.localizedDescription)
            }
        }
}
