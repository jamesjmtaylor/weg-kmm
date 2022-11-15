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
    var hasNextPage = false
    /// Tells if all records have been loaded. (Used to hide/show activity spinner)
//     var hasNextPage = true
    
    func fetchEquipment(){}
    func fetchNextData(){}
}

/// Extends ``weg_ios/EquipmentViewModel`` so that the ``wegApp`` can inject the ``weg_ios/wegApp/sdk`` for making network and database requests.
class EquipmentViewModel: PreviewEquipmentViewModel {
    private let sdk: EquipmentSDK
    
    init(sdk: EquipmentSDK) {
        self.sdk = sdk
    }
    
    override func fetchEquipment() {
        sdk.pagingData.watch { nullablePagingData in
            guard let list = nullablePagingData?.compactMap({ $0 as? SearchResult }) else {
                return
            }
            self.equipment = list
            self.hasNextPage = self.sdk.pager.hasNextPage
        }
    }
    override func fetchNextData() {
        sdk.pager.loadNext()
    }
}
