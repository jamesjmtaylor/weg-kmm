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
    
    /// Tells if all records have been loaded. (Used to hide/show activity spinner)
    var equipmentListFull = false
    /// Tracks last page loaded. Used to load next page (current + 1)
    var currentPage = PageProgress()
    init(equipment: [SearchResult]) {
        self.equipment = equipment
    }
    
    func loadEquipment(forceReload: Bool){}
}

/// Extends ``weg_ios/EquipmentViewModel`` so that the ``wegApp`` can inject the ``weg_ios/wegApp/sdk`` for making network and database requests.
class EquipmentViewModel: PreviewEquipmentViewModel {
    private let sdk: EquipmentSDK
    
    init(sdk: EquipmentSDK) {
        self.sdk = sdk
        super.init(equipment: [SearchResult]())
    }
    //TODO: Fix "Field 'images' is required for type with serial name 'com.jamesjmtaylor.weg.models.SearchResult', but it was missing"
    override func loadEquipment(forceReload: Bool) {
        
        let force : KotlinBoolean = forceReload ? true : false
        sdk.getEquipment(equipmentType: .land, page: currentPage.getPageFor(equipmentType: .land), forceReload: force, completionHandler: { [weak self] equipment, error in
            self?.equipmentListFull = equipment?.isEmpty ?? true
            self?.currentPage.incrementPageFor(equipmentType: .land)
            if let equipment = equipment {
                self?.equipment = (self?.equipment ?? [SearchResult]()) + equipment
            } else {
                print(error?.localizedDescription ?? "error")
            }
        })
    }
}
//TODO: It might be better to write our own kotlin native pagination SDK since iOS doesn't have a native way to do it.
///Keeps track of the pagination progress by equipment type.  It cannot be contained within the SDK because each app has its own native pagination library.
class PageProgress {
    var land: Int32 = 0
    var air: Int32 = 0
    var sea: Int32 = 0
    func getPageFor(equipmentType: EquipmentType) -> Int32 {
        switch equipmentType {
        case .land: return land
        case .air: return air
        case .sea: return sea
        default: return 0
        }
    }
    func incrementPageFor(equipmentType: EquipmentType) {
        switch equipmentType {
        case .land: land = land + 1
        case .air: air = air + 1
        case .sea: sea = sea + 1
        default: break
        }
    }
}
