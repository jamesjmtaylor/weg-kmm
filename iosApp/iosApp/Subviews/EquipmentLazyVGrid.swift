//
//  EquipmentLazyVGrid.swift
//  weg-ios
//
//  Created by James Taylor on 11/3/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared


//TODO: Pull to refresh

struct EquipmentLazyVGrid: View {
    @Environment(\.horizontalSizeClass) var horizontalSizeClass
    @ObservedObject private(set) var vm: PreviewEquipmentViewModel
    @State private var searchText = ""
    
    let columns = [GridItem(.flexible()), GridItem(.flexible())]
    var body: some View {
        NavigationView{
            ScrollView {
                LazyVGrid(columns: getColumns(), spacing: 0) {
                    ForEach (vm.equipment, id: \.id) { equipment in
                        EquipmentCard(equipment: equipment)
                    }
                    if vm.hasNextPage {
                        nextPageView
                    }
                }
            }
        }
        .searchable(text: $searchText, prompt: "Search Equipment")
        .onAppear { vm.fetchEquipment() }
    }
    
    private var nextPageView: some View {
        HStack {
            Spacer()
            VStack {
                ProgressView()
                Text("Loading next page...")
            }
            Spacer()
        }
        .onAppear(perform: {
            vm.fetchNextData()
        })
    }

    func getColumns() -> [GridItem] {
        var columns = [GridItem]()
    
        let numColumns = horizontalSizeClass == .compact ? 2 : 4
        for _ in 1...numColumns {
            columns.append(GridItem(.flexible(),spacing: 0))
        }
        return columns
    }
}

struct EquipmentLazyVGrid_Previews: PreviewProvider {
    static var previews: some View {
        let vm = PreviewEquipmentViewModel()
//        vm.equipment = WegApp.placeholderEquipment
        EquipmentLazyVGrid(vm: vm)
    }
}
