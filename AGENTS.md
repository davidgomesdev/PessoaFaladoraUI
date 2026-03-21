# AGENTS.md — Pessoa Faladora UI

## Project Overview

Kotlin Multiplatform (KMP) + Compose Multiplatform UI for **Pessoa Faladora**, a chatbot that answers questions as the
Portuguese poet Fernando Pessoa. The UI streams responses from a backend API and presents them in a dark-themed web app.

**Targets:** JS (primary), JVM (desktop via Compose Desktop, for hot reload when developing).

---

## Architecture

```
composeApp/src/
  commonMain/   ← All UI logic, Ktor HTTP client, shared widgets
  webMain/      ← Web-specific code (for JS)
  jsMain/       ← JS-target-specific code (Config.kt actual)
  jvmMain/      ← JVM desktop entry point (Window); also contains Config.kt actual
  webTest/      ← Tests running on JS target
```

`webMain` is not used — not to be confused with `jsMain`. Both Web, JS, and JVM targets share `commonMain` but have
separate `main.kt` and `Config.kt` platform actuals.

### Key Files

| File                                 | Role                                                             |
|--------------------------------------|------------------------------------------------------------------|
| `commonMain/.../App.kt`              | Root `@Composable` — all state, coroutine logic, layout          |
| `commonMain/.../service/ThinkAPI.kt` | Ktor HTTP client; streams `/pensa` PUT response as `channelFlow` |
| `commonMain/.../service/Config.kt`   | `expect fun getPessoaUrl(): String` — platform-specific URL      |
| `jsMain/.../service/Config.kt`       | Reads `window.PESSOA_URL` JS global, falls back to default       |
| `jvmMain/.../service/Config.kt`      | Reads `PESSOA_URL` env var, falls back to default                |
| `commonMain/.../widget/Drawing.kt`   | `AppTitle`, `FernandoPessoaLogo` composables                     |
| `commonMain/.../widget/Input.kt`     | `ThinkQueryTextField`, `ThinkButton` composables                 |
| `commonMain/.../Colors.kt`           | Dark-theme color palette constants                               |
| `commonMain/.../Theme.kt`            | `RobotoTypography()` — uses `RobotoFlexVariable` font resource   |
| `commonMain/.../UIUtils.kt`          | `isActionInputType()` — Ctrl/Cmd+Enter submit shortcut           |

---

## Backend Integration

- Default backend URL: `http://127.0.0.1:8080`
- Endpoint: `PUT /pensa` with JSON body `{"input": "<query>"}` — **streaming** response
- Response protocol: plain text chunks for the answer; sources delivered as a `<sources>...</sources>` XML-like tag
- `ThinkAPI.sendThinkRequest()` returns a `Flow<String>` via `channelFlow`; collectors parse the `<sources>` sentinel
  in-stream:
  ```kotlin
  if (it.contains("<sources>")) {
      response.sources = it.removePrefix("<sources>").removeSuffix("</sources>")
  } else {
      response.message += it
  }
  ```
- 60-second socket timeout configured on the Ktor client

---

## Developer Workflows

### Run (development, with hot-reload)

```shell
./gradlew hotRunJvm --mainClass "me.davidgomesdev.pessoafaladora.ui.MainKt" --quiet
```

### Production build for Web

```shell
./gradlew :composeApp:jsBrowserProductionWebpack
```

### Tests

```shell
./gradlew :composeApp:jsBrowserTest
```

---

## Conventions & Patterns

- **`expect`/`actual` for platform config**: Platform-sensitive behavior lives in `service/Config.kt` per source set.
  Add new platform-specific functions there using the same `expect`/`actual` pattern.
- **UI strings are in Portuguese**: Labels, placeholders, and error messages use Portuguese (e.g., `"A pensar..."`,
  `"Escreve o que te inquieta a alma..."`). Keep this consistent.
- **No ViewModel**: State is managed directly in `App()` with `remember`/`mutableStateOf`. Do not introduce a ViewModel
  unless expanding to non-web targets.
- **Colors from `Colors.kt`**: Never use hardcoded hex colors in composables — use `backgroundColor`,
  `componentsBackgroundColor`, `componentColumnBackgroundColor`, `focusedIndicatorColor`, or introduce a new variable.
- **Resources via generated class**: Access drawables and fonts through
  `pessoafaladora.composeapp.generated.resources.Res` (e.g., `Res.drawable.logo`, `Res.font.RobotoFlexVariable`). Source
  assets live in `commonMain/composeResources/`.
- **Webpack resource copying** (`composeApp/webpack.config.d/copy-resources.js`): JS target copies processed resources
  into the bundle automatically — no manual asset management needed.
- **Dependency versions**: All versions are centralized in `gradle/libs.versions.toml`. Use version catalog aliases (
  e.g., `libs.ktor.client.core`) rather than inline version strings in `build.gradle.kts`.
