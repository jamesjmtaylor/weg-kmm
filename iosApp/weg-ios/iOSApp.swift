import SwiftUI
import shared

@main
struct iOSApp: App {
    let sdk = EquipmentSDK(databaseDriverFactory: DatabaseDriverFactory())
	var body: some Scene {
        WindowGroup<EquipmentView> {
            EquipmentView(vm: .init(sdk: sdk))
		}
	}
    
    init() {
        URLCache.shared.memoryCapacity = 10_000_000 // ~10 MB memory space
        URLCache.shared.diskCapacity = 1_000_000_000 // ~1GB disk cache space
    }
}
