import SwiftUI
import shared

struct EquipmentView: View {
    @ObservedObject private(set) var vm: PreviewEquipmentViewModel
	var body: some View {
        
        TabView {
            EquipmentLazyVGrid(vm: vm)
                .tabItem{
                    Image("ic_land")
                    Text("Land")
                }
        }
	}
}



struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
        let vm = PreviewEquipmentViewModel(equipment: WegApp.placeholderEquipment)
        EquipmentView(vm: vm)
	}
}
