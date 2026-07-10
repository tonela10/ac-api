# iOS app

Xcode can't build a Kotlin Multiplatform project directly — you need a native Xcode project that embeds the `ComposeApp` XCFramework produced by Gradle.

## Setup (one-time)

1. Open Xcode → **Create New Project** → **iOS App**
2. Product name: `iosApp`, interface: SwiftUI, language: Swift
3. Save into this directory (`app/iosApp/`), overwriting the placeholder folder
4. Delete Xcode's generated `ContentView.swift` and `iosAppApp.swift`
5. Drag `iOSApp.swift` (already in this folder) into the Xcode project
6. In **Build Phases** → **Run Script**, add a new phase *before* "Compile Sources":
   ```bash
   cd "$SRCROOT/.."
   ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
   ```
7. In **Framework Search Paths**, add:
   ```
   $(SRCROOT)/../composeApp/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)
   ```
8. In **Other Linker Flags**, add `-framework ComposeApp`
9. Build & run on a simulator

Alternatively, use the [JetBrains Kotlin Multiplatform Wizard](https://kmp.jetbrains.com/) to generate a fresh scaffold and copy the `commonMain` / `androidMain` / `iosMain` sources from this repo into it — that gets you a working `iosApp.xcodeproj` in minutes instead of clicking through the manual steps.
