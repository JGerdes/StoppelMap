//
//  HomeScreen.swift
//  StoppelMap
//
//  Created by Jonas Gerdes on 15.03.24.
//

import SwiftUI

struct HomeScreen: View {
    var body: some View {
        NavigationStack {
            VStack(){
                CountdownCard(
                    days: 512, 
                    hours: 18,
                    minutes: 44
                )
            }
            .frame(
                maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/,
                maxHeight: .infinity,
                alignment: .top
            )
            .navigationBarTitle("Stoppelmarkt 2024", displayMode: .large)
        }
    }
}


#Preview {
    HomeScreen()
}
