//
//  EquipmentCard.swift
//  weg-ios
//
//  Created by James Taylor on 11/2/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct EquipmentCard: View {
    var imageUrl: String
    var text: String
    var body: some View {
        ZStack(alignment: .bottomLeading) {
            Text(text)
            let url = URL(string: imageUrl)
            AsyncImage(url: url) { image in
                image.resizable()
            } placeholder: {
                Image("placeholder")
            }
        }}
}

struct EquipmentCard_Previews: PreviewProvider {
    static var previews: some View {
        EquipmentCard(imageUrl: "", text: "Test")
    }
}
