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
    @Published var equipment : [SearchResult] = []
    /// Tells if all records have been loaded. (Used to hide/show activity spinner)
    var hasNextPage = false
    
    func fetchEquipment(){}
    func fetchNextData(){}
}

/// Extends ``weg_ios/EquipmentViewModel`` so that the ``wegApp`` can inject the ``weg_ios/wegApp/sdk`` for making network and database requests.
class EquipmentViewModel: PreviewEquipmentViewModel {
    internal let sdk: EquipmentSDK
    init(sdk: EquipmentSDK) {
        self.sdk = sdk
    }
}

class LandEquipmentViewModel: EquipmentViewModel {
    override func fetchEquipment() {
        sdk.landPagingData.watch { nullablePagingData in
            guard let list = nullablePagingData?.compactMap({ $0 as? SearchResult }) else {
                return
            }
            self.equipment = list
            self.hasNextPage = self.sdk.landPager.hasNextPage
        }
    }
    override func fetchNextData() {
        sdk.landPager.loadNext()
    }
}

class AirEquipmentViewModel: EquipmentViewModel {
    override func fetchEquipment() {
        sdk.airPagingData.watch { nullablePagingData in
            guard let list = nullablePagingData?.compactMap({ $0 as? SearchResult }) else {
                return
            }
            self.equipment = list
            self.hasNextPage = self.sdk.airPager.hasNextPage
        }
    }
    override func fetchNextData() {
        sdk.airPager.loadNext()
    }
}

class SeaEquipmentViewModel: EquipmentViewModel {
    override func fetchEquipment() {
        sdk.seaPagingData.watch { nullablePagingData in
            guard let list = nullablePagingData?.compactMap({ $0 as? SearchResult }) else {
                return
            }
            self.equipment = list
            self.hasNextPage = self.sdk.seaPager.hasNextPage
        }
    }
    override func fetchNextData() {
        sdk.seaPager.loadNext()
    }
}
