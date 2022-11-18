import SwiftUI
import shared

@main
struct WegApp: App {
    //TODO: "InvalidMutabilityException: mutation attempt of frozen com.jamesjmtaylor.weg.EquipmentSDK"
    let sdk = EquipmentSDK(databaseDriverFactory: DatabaseDriverFactory())
    var body: some Scene {
        WindowGroup<EquipmentView> {
            EquipmentView(
                landVm: LandEquipmentViewModel(sdk: sdk),
                airVm: AirEquipmentViewModel(sdk: sdk),
                seaVm: SeaEquipmentViewModel(sdk: sdk)
            )
        }
    }
    
    init() {
        URLCache.shared.memoryCapacity = 10_000_000 // ~10 MB memory space
        URLCache.shared.diskCapacity = 1_000_000_000 // ~1GB disk cache space
    }
    
    static let placeholderEquipment = [
        SearchResult(title: "Tank", id: 1, categories: ["Land"], images: [Image(name: "Tank", url: "")], details: nil, page: 0),
        SearchResult(title: "Ship", id: 2, categories: ["Sea"], images: [Image(name: "Ship", url: "")], details: nil, page: 0),
        SearchResult(title: "Plane", id: 3, categories: ["Air"], images: [Image(name: "Plane", url: "")], details: nil, page: 0),
        SearchResult(title: "Truck", id: 4, categories: ["Land"], images: [Image(name: "Truck", url: "")], details: nil, page: 0),
        SearchResult(title: "Gun", id: 5, categories: ["Land"], images: [Image(name: "Gun", url: "")], details: nil, page: 0)
    ]
}



