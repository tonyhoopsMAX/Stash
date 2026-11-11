# STASH

A minimal Android Jetpack Compose prototype for capturing thoughts in a local inbox. It opens directly into the editor, keeps a chronological vault, and includes an obsidian-glass security/paywall concept.

## Run

Open the project in Android Studio with JDK 17 and Android SDK 35 installed, let Gradle sync, and run the `app` configuration on an API 26+ device or emulator. From a terminal, use `./gradlew assembleDebug` (or `gradlew.bat assembleDebug` on Windows).

## Prototype limitations

Notes are persisted locally in **unencrypted** app-private preferences. Android backup is disabled, but there is no biometric lock, hardware encryption, billing, entitlement validation, or purchase restoration. The paywall prices are illustrative and its purchase and restore controls are disabled. Do not use this prototype for sensitive information or represent it as secure. Integrate and verify those services before enabling the paywall callbacks or making security claims.
