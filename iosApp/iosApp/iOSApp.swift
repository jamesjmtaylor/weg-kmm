import SwiftUI
import shared

@main
struct iOSApp: App {
    let sdk = EquipmentSDK(databaseDriverFactory: DatabaseDriverFactory())
	var body: some Scene {
        WindowGroup<ContentView> {
            ContentView(vm: .init(sdk: sdk))
		}
	}
}
