# UbiAssignment - Ubiquiti Mobile Test Application

An Android application built with Jetpack Compose, Kotlin Coroutines, Koin, Ktor, and Room, following Clean Architecture and MVI/MVVM principles. The app displays Ubiquiti products, allowing users to search, filter by product categories, view technical specifications, and browse offline.

---

## 🚀 How to Run the App

### Prerequisites
* **Android Studio**: Ladybug / Jellyfish or newer recommended.
* **JDK**: Version 17+.
* **Android SDK**: `minSdk = 30` (Android 11+), `targetSdk = 37`.
* **Device / Emulator**: Physical device or emulator running Android 11+.

### Steps to Build and Run
1. **Open Project**: Launch Android Studio and open the project directory.
2. **Gradle Sync**: Let Gradle sync dependencies automatically (or trigger via `File > Sync Project with Gradle Files`).
3. **Run from Android Studio**:
   - Select the `app` run configuration.
   - Choose your connected Android device or emulator.
   - Click **Run** (`Shift + F10`).
4. **Run from Command Line**:
   - **Assemble Debug APK**:
     ```bash
     ./gradlew assembleDebug
     ```
   - **Install and Run on Connected Device**:
     ```bash
     ./gradlew installDebug
     ```
   - **Run Unit Tests**:
     ```bash
     ./gradlew testDebugUnitTest
     ```

---

## 🛠️ Assumptions & Tradeoffs

1. **Single Activity with Animated Master-Detail**:
   * **Decision**: Navigating between `ProductListScreen` and `ProductDetailsScreen` is handled via `AnimatedVisibility` (horizontal slide) inside `MainActivity`.

2. **Offline-First & Local Caching**:
   * **Decision**: Network responses are cached locally via **Room**. When the network is unavailable, the application seamlessly falls back to cached data and presents an `OfflineModeIndicator` banner.
   * **Tradeoff**: Network check uses `NetworkCapabilities` to alert users of offline status dynamically.

3. **Hybrid Image Asset Resolution**:
   * **Decision**: Product images check preloaded APK assets first (`assets/product-id/imageId.png`), fallback to local override files if present, and finally fall back to the remote Ubiquiti image server.
   * **Tradeoff**: Saves bandwidth and accelerates load times for bundled assets while maintaining full dynamic coverage.

4. **Client-Side Search & Multi-Keyword Filtering**:
   * **Decision**: Searching is performed in-memory across product names, SKUs, model names, device types, line names, IDs, ports, and network capabilities (`"10g"`, `"24 port switch"`).
   * **Tradeoff**: Delivers instant zero-latency feedback as the user types or toggles category tabs ("All", "Cameras", "IoT").

---

## 📚 Libraries Used & Why

| Library | Purpose & Rationale |
| :--- | :--- |
| **Jetpack Compose & Material 3** | Modern declarative UI toolkit providing fluid animations, responsive layouts, and Material Design 3 components. |
| **Koin (`koin-android`, `koin-androidx-compose`)** | Pragmatic dependency injection framework designed for Kotlin and Compose with minimal boilerplate and fast build times compared to Hilt. |
| **Ktor Client (`ktor-client-android`, `kotlinx.serialization`)** | Asynchronous HTTP networking engine with type-safe Kotlinx JSON serialization. |
| **Room Database (`androidx.room`)** | Local SQLite persistence layer for offline caching of product data. |
| **Glide Compose (`com.github.bumptech.glide:compose`)** | Asynchronous image loading, disk caching, and `GlideSubcomposition` for custom loading indicators and error states. |
| **Kermit / Timber** | Structured, lightweight logging utilities across Android layers. |
| **JUnit 4 & Ktor Mock Client** | Unit testing for domain logic, repository caching, and network response scenarios. |
