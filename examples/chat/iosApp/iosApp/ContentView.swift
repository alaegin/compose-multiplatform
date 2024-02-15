import SwiftUI

struct ContentView: View {
    @State var toggle: Bool = false

    var body: some View {
        ZStack {
            if toggle {
                ComposeInsideSwiftUIScreen()
            } else {
                YetAnotherSwiftUIScreen()
            }

            Button("Toggle", action: { toggle = !toggle })
        }
    }
}

let gradient = LinearGradient(
        colors: [
            Color(red: 0.933, green: 0.937, blue: 0.953),
            Color(red: 0.902, green: 0.941, blue: 0.949)
        ],
        startPoint: .topLeading, endPoint: .bottomTrailing
)
