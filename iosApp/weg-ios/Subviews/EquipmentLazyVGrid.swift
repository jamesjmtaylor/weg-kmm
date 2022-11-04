//
//  EquipmentLazyVGrid.swift
//  weg-ios
//
//  Created by James Taylor on 11/3/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

//TODO: Implement infinite scroll per https://medium.com/swlh/easy-pagination-in-swiftui-da9e1fe3e25e
struct EquipmentLazyVGrid: View {
    @Environment(\.horizontalSizeClass) var horizontalSizeClass
    @ObservedObject private(set) var vm: PreviewEquipmentViewModel
    let columns = [GridItem(.flexible()), GridItem(.flexible())]
    var body: some View {
        ScrollView {
            LazyVGrid(columns: getColumns(), spacing: 0) {
                ForEach (vm.equipment, id: \.id) { equipment in
                    EquipmentCard(equipment: equipment)
                }
            }
        }
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
        let vm = PreviewEquipmentViewModel(equipment: WegApp.placeholderEquipment)
        EquipmentLazyVGrid(vm: vm)
    }
}
