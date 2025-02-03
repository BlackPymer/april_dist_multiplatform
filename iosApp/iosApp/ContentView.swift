import UIKit
import SwiftUI
import ShirmazApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) { }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
                .ignoresSafeArea(.all, edges: .bottom)
                .onAppear{
                    UIApplication.shared.isIdleTimerDisabled = true
                }
                .onDisappear{
                    UIApplication.shared.isIdleTimerDisabled = false
                }
    }
}



