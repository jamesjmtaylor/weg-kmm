//
//  WegApp.swift
//  iosApp
//
//  Created by James Taylor on 1/8/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

@main
struct WegApp: App {
    let sdk = EquipmentSDK()
    var body: some Scene {
        WindowGroup<EquipmentView> {
//            EquipmentView(
//                landVm: LandEquipmentViewModel(sdk: sdk),
//                airVm: AirEquipmentViewModel(sdk: sdk),
//                seaVm: SeaEquipmentViewModel(sdk: sdk)
//            )
        }
    }
    
    init() {
        URLCache.shared.memoryCapacity = 10_000_000 // ~10 MB memory space
        URLCache.shared.diskCapacity = 1_000_000_000 // ~1GB disk cache space
    }
    
//    static let placeholderEquipment = [
//        Contentlet(title: "Tank", id: 1, categories: ["Land"], images: [Image(name: "Tank", url: "")], details: nil, page: 0),
//        Contentlet(title: "Ship", id: 2, categories: ["Sea"], images: [Image(name: "Ship", url: "")], details: nil, page: 0),
//        Contentlet(title: "Plane", id: 3, categories: ["Air"], images: [Image(name: "Plane", url: "")], details: nil, page: 0),
//        Contentlet(title: "Truck", id: 4, categories: ["Land"], images: [Image(name: "Truck", url: "")], details: nil, page: 0),
//        Contentlet(title: "Gun", id: 5, categories: ["Land"], images: [Image(name: "Gun", url: "")], details: nil, page: 0)
//    ]
}



