# 🚀 Login, Register & Real-Time Chat Android App

## ✨ Modern User Management with Jetpack Compose, MVVM, & WebSockets

This open-source Android project is a comprehensive template for building **secure user authentication** and **real-time communication** systems using **Jetpack Compose** and modern Android development best practices.

It features a polished UI with **Shimmer effects**, **Pull-to-refresh**, and a robust **Chat system** powered by Ktor WebSockets.

---

### 🔑 Key Features

- 🔐 **Secure Authentication**: Complete Login & Registration flow with real-time form validation.
- 💬 **Real-Time Chat**: Messaging system with WebSocket integration, optimistic UI updates, and online/offline status.
- 👥 **User Management**: View a dynamic list of registered users with search-like capabilities.
- 🪄 **Modern UI/UX**:
    - **Shimmer Effect**: Beautiful skeleton loading states for a premium feel.
    - **Pull-to-Refresh**: Seamless data synchronization.
    - **Material Design 3**: Leveraging the latest Material You components and themes.
- ⚡ **Performance Optimized**:
    - **MVVM Architecture**: Clean separation of concerns.
    - **Dagger Hilt**: Industry-standard dependency injection.
    - **Kotlin Coroutines & Flows**: Smooth asynchronous programming.
    - **Jetpack DataStore**: Efficient and safe local data persistence.

---

### 🛠️ Tech Stack

- **UI**: Jetpack Compose (1.7+)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Dagger Hilt
- **Network**: Retrofit & Ktor WebSockets
- **Local Storage**: Jetpack DataStore
- **Concurrency**: Kotlin Coroutines & SharedFlow/StateFlow
- **Image Loading**: Glide for Compose
- **Navigation**: Modern Navigation 3 Implementation

---

### 🔗 Backend API Integration

This app requires a backend API for authentication and messaging. The backend is built with Ktor and is available here:  
➡️ **[Ktor API Client Backend](https://github.com/KishanViramgama/ktor-api-client)**

**Crucial Steps:**
1. Clone and run the backend server.
2. Ensure your Android device/emulator can reach the server IP.
3. Update the `BASE_URL` in the app's utility classes to match your server IP.

---

### ⚙️ Database Configuration (Firebase vs. SQL)

This project is uniquely designed to support both **Firebase** and **Local SQL (MySQL/PostgreSQL)** backends. You can toggle between them with a single flag:

1.  Open `BasePath.kt` in the `network.utility` package.
2.  Locate the `IS_FIREBASE` flag:
    -   **Set to `true`**: Connects to the Firebase-style endpoints on your Ktor server.
    -   **Set to `false`**: Connects to the SQL-style endpoints (`/mySql/`).
3.  Update the `BASE` IP address to match your server's local IP.

---

### 🚀 Getting Started

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/KishanViramgama/LoginRegister.git
   ```
2. **Setup Backend:** Follow the instructions in the [Backend Repo](https://github.com/KishanViramgama/ktor-api-client) to get the server running.
3. **Configure App:**
    - Open the project in **Android Studio**.
    - Locate `BasePath.kt` (or your network config) and update the IP address to your local machine's IP.
4. **Build & Run:** Deploy to an Android device (API 23+) or emulator.

---

### 🔍 SEO Keywords

`Jetpack Compose Login`, `Android Chat App`, `Real-time Messaging Android`, `Kotlin MVVM Authentication`, `Dagger Hilt Tutorial`, `Shimmer Effect Compose`, `Ktor WebSockets Android`, `DataStore Preferences`, `Material Design 3 Demo`, `Swipe to Refresh Compose`, `Android User List`, `Kotlin Coroutines Flow Chat`

---

### ☕ Support My Work

If you find this project useful, please consider giving it a ⭐ or buying me a coffee to support future development:

<p align="center">
  <a href="https://paypal.me/KishanViramgama?country.x=IN&locale.x=en_GB" target="_blank">
    <img src="https://img.shields.io/badge/☕-Buy%20Me%20a%20Coffee-orange?style=for-the-badge" alt="Buy me a coffee" />
  </a>
</p>

---

*Built with ❤️ by [Kishan Viramgama](https://github.com/KishanViramgama)*
