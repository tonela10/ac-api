# AC API Demo — Kotlin Multiplatform + Compose Multiplatform

Two-screen reference client for the [ac-api](../README.md) public API. Villagers list → tap for detail. Same Kotlin code runs on Android and iOS via Compose Multiplatform.

## Stack

- **Kotlin 2.1** with Kotlin Multiplatform
- **Compose Multiplatform 1.7** — one UI codebase for both platforms
- **Ktor Client 3** — the KMP-native HTTP client (Retrofit is JVM-only, not portable to iOS)
- **androidx.lifecycle ViewModel + Navigation Compose** — official JetBrains KMP artifacts

## Run — Android

```bash
cd app
./gradlew :composeApp:assembleDebug
```

Or open `app/` in Android Studio Ladybug+ and hit Run.

## Run — iOS

You need to create the Xcode project first — see [`iosApp/README.md`](iosApp/README.md).

## Where things live

```
composeApp/src/
├── commonMain/  # UI, ViewModels, repos, DTOs — all shared
├── androidMain/ # MainActivity, OkHttp engine
└── iosMain/     # MainViewController, Darwin engine
```

The KMP `expect`/`actual` pattern is used only in one place: [`platform/HttpEngine.kt`](composeApp/src/commonMain/kotlin/com/example/acapp/platform/HttpEngine.kt), where each platform provides its own Ktor engine.

## API base URL

Hardcoded to `https://ac.jolgorio.app` in [`AcApiClient.kt`](composeApp/src/commonMain/kotlin/com/example/acapp/data/AcApiClient.kt). Change the `API_HOST` constant to point elsewhere for local dev (e.g. `10.0.2.2` for the Android emulator hitting `localhost` on your Mac).
