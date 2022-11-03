//
//  EquipmentCard.swift
//  weg-ios
//
//  Created by James Taylor on 11/2/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct EquipmentCard: View {
    var equipment: SearchResult
    var body: some View {
        ZStack(alignment: .bottom) {
            let url = URL(string: equipment.images.first?.url ?? "")
            AsyncImage(url: url) { image in
                image.resizable()
            } placeholder: {
                Image("placeholder")
            }
            Text(equipment.title ?? "")
        }}
}

struct EquipmentCard_Previews: PreviewProvider {
    static var previews: some View {
        let placeholder = SearchResult(title: "Tank", id: 1, categories: ["Land"], images: [Image(name: "Tank", url: "")], details: nil)
        EquipmentCard(equipment: placeholder)
    }
}
