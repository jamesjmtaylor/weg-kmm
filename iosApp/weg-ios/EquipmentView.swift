import SwiftUI
import shared

struct EquipmentView: View {
    @ObservedObject private(set) var landVm: PreviewEquipmentViewModel
    @ObservedObject private(set) var airVm: PreviewEquipmentViewModel
    @ObservedObject private(set) var seaVm: PreviewEquipmentViewModel
	var body: some View {
        TabView {
            EquipmentLazyVGrid(vm: landVm)
                .tabItem{
                    Image("ic_land")
                    Text("Land")
                }
            EquipmentLazyVGrid(vm: airVm)
                .tabItem{
                    Image("ic_air")
                    Text("Air")
                }
            EquipmentLazyVGrid(vm: seaVm)
                .tabItem{
                    Image("ic_sea")
                    Text("Sea")
                }
        }
	}
}



struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
        let vm = PreviewEquipmentViewModel()
//        vm.equipment = WegApp.placeholderEquipment
        EquipmentView(landVm: vm, airVm: vm, seaVm: vm)
	}
}
