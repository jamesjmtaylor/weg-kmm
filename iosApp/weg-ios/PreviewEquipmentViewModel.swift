//
//  EquipmentViewModel.swift
//  iosApp
//
//  Created by James Taylor on 8/17/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

/// Used to allow SwiftUI previews without requiring a ``weg_ios/wegApp/sdk`` to be injected.
class PreviewEquipmentViewModel: NSObject, ObservableObject {
    @Published var equipment : [SearchResult]
    
    init(equipment: [SearchResult]) {
        self.equipment = equipment
    }
    
    func loadEquipment(equipmentType: EquipmentType, page: Int, forceReload: Bool){}
}

/// Extends ``weg_ios/EquipmentViewModel`` so that the ``wegApp`` can inject the ``weg_ios/wegApp/sdk`` for making network and database requests.
class SdkEquipmentViewModel: PreviewEquipmentViewModel {
    let sdk: EquipmentSDK
    
    init(sdk: EquipmentSDK) {
        self.sdk = sdk
        super.init(equipment: [SearchResult]())
        self.loadEquipment(equipmentType: EquipmentType.land, page: 0, forceReload: false)
    }
    
    override func loadEquipment(equipmentType: EquipmentType, page: Int, forceReload: Bool) {
        let force : KotlinBoolean = forceReload ? true : false
        sdk.getEquipment(equipmentType: EquipmentType.land, page: 0, forceReload: force, completionHandler: { equipment, error in
            if let equipment = equipment {
                self.equipment = equipment
            } else {
                print(error?.localizedDescription ?? "error")
            }
        })
    }
}
