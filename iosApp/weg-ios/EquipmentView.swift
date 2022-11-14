import SwiftUI
import shared

struct EquipmentView: View {
    @ObservedObject private(set) var vm: PreviewEquipmentViewModel
	var body: some View {
        //TODO: Return different results based on tab selection
        TabView {
            EquipmentLazyVGrid(vm: vm)
                .tabItem{
                    Image("ic_land")
                    Text("Land")
                }
            EquipmentLazyVGrid(vm: vm)
                .tabItem{
                    Image("ic_air")
                    Text("Air")
                }
            EquipmentLazyVGrid(vm: vm)
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
        EquipmentView(vm: vm)
	}
}
