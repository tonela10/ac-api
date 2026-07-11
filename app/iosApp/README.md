# iOS app

## Run

```bash
open iosApp.xcodeproj
```

Then hit ⌘R in Xcode. On first build the "Compile Kotlin Framework" phase runs `./gradlew :composeApp:embedAndSignAppleFrameworkForXcode` — that takes a minute the first time, then it's incremental.

## Requirements

- Xcode 15+
- iOS 16 deployment target
- The Kotlin/Native toolchain (installed automatically the first time you build — the Gradle script pulls it via the standard KMP mechanism)

## How it's wired

- `iOSApp.swift` — SwiftUI `@main` entry, wraps the Kotlin-produced UIViewController in a `UIViewControllerRepresentable`
- `iosApp.xcodeproj` — pre-configured with:
  - Framework Search Path pointing at `../composeApp/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)`
  - `-framework ComposeApp` in Other Linker Flags
  - "Compile Kotlin Framework" script build phase that runs before compile

## Signing

The project uses automatic signing. On first open, Xcode may prompt you to select a team — pick your personal Apple ID. For simulator builds, no signing is needed.
