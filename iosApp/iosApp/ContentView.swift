import SwiftUI
import shared

struct ContentView: View {
    @ObservedObject private(set) var vm: EquipmentViewModel
	var body: some View {
        Text("Results: \(vm.equipment.count)")
	}
}

//struct ContentView_Previews: PreviewProvider {
//	static var previews: some View {
//		ContentView()
//	}
//}
