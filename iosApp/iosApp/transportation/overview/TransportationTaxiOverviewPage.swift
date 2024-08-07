import SwiftUI
import Shared

struct TransportationTaxiOverviewPage: View {
    
    var viewState: TransportationOverviewViewModel.TaxiServicesState
    
    var body: some View {
        List {
            ForEach(viewState.services, id: \.title) { service in
                HStack {
                    VStack(alignment: .leading) {
                        Text(service.title)
                        Text(service.phoneNumberFormatted ?? service.phoneNumber).font(.footnote)
                    }
                    Spacer()
                    Image(systemName: "phone.fill")
                }
                .contentShape(Rectangle())
                .onTapGesture {
                    if let url = URL(string: "tel:\(service.phoneNumber)") {
                        UIApplication.shared.open(url   )
                    }
                }
            }
        }
    }
}

