import SwiftUI
import shared

struct EquipmentView: View {
    @ObservedObject private(set) var vm: EquipmentViewModel
	var body: some View {
        
        TabView {
            Text("Results: \(vm.equipment.count)")
                .tabItem{
                    Image("ic_land")
                    Text("Land")
                }
        }
	}
}



//struct ContentView_Previews: PreviewProvider {
//	static var previews: some View {
//		ContentView()
//	}
//}
