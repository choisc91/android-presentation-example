# 📺 PresentationExample

> A modern Android sample demonstrating how to render **independent content on multiple displays** using `Presentation`, and how to **integrate legacy XML with Jetpack Compose** seamlessly.

---

## 🚀 Features

- 📱 **Main Screen (Primary Display)**  
  Built using Jetpack Compose — a fully modern UI powered by `MainComposeScreen`.

- 🖥️ **Secondary Screens (External Displays)**  
  Uses Android’s `Presentation` class to display different layouts on connected external displays (e.g., HDMI, USB-C).

- 🔀 **Compose ↔ XML Interop**  
  Combines traditional XML layout with Composable UI via `AndroidView`.

- 🔌 **Auto Detection of Displays**  
  Automatically detects external displays (via `DisplayManager.DisplayListener`) and renders the appropriate `Presentation`.

---

## 🧑‍💻 Tech Stack

- Jetpack Compose
- Android Presentation API
- DisplayManager
- XML ↔ Compose Interop (`AndroidView`)
- Lifecycle + SavedState integration for ComposeView

---

## 📷 Screenshots

| Main Screen (Compose) | External Display (XML) |
|-----------------------|------------------------|
| ![main](docs/screenshot_main.png) | ![presentation](docs/screenshot_presentation.png) |

---

## 🛠️ How to Run

1. **Connect an external display** (HDMI / USB-C)
2. **Run the app** on a compatible Android device (Tablet / Phone with DisplayPort Alt mode)
3. Watch content dynamically rendered on each display

> Note: Presentation only works on devices that support external display mirroring (like Samsung DeX, Pixel HDMI out, etc).

---

## 🧩 Customization

- You can customize what’s shown per display by editing the `initPresentations()` logic in `MainActivity.kt`.
- Add your own layout per `display.uniqueId` and handle it with a matching `Presentation` subclass.

---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

---

## ✨ Author

**Characin.choi**  
Android Open Source | Embedded Multi-Display UI Expert  
[GitHub Profile](https://github.com/choisc91)



