//
//  EquipmentViewModel.swift
//  iosApp
//
//  Created by James Taylor on 8/17/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

class EquipmentViewModel: NSObject, ObservableObject {
    let sdk: EquipmentSDK
    @Published var equipment = [SearchResult]()
    
    init(sdk: EquipmentSDK) {
        self.sdk = sdk
        super.init()
        self.loadEquipment(forceReload: false)
    }
    
    func loadEquipment(forceReload: Bool) {
        sdk.getEquipment(forceReload: forceReload, completionHandler: { equipment, error in
            if let equipment = equipment {
                self.equipment = equipment
            } else {
                print(error?.localizedDescription ?? "error")
            }
        })
    }
}
