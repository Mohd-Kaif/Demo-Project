# Star Wars App 🚀

Star Wars App is a Jetpack Compose-based Android app that uses the **SWAPI API** to display a list of Star Wars characters and their details.

## ✨ Features

- 📜 **Jetpack Compose UI**: Modern, declarative UI with Material Design.
- 🔍 **Pagination**: Fetches data page-by-page and loads more as you scroll.
- ⚡ **MVVM Architecture**: Clean separation of concerns.
- 🔄 **Caching**: Uses **OkHttp caching** for efficient offline access.
- 🔧 **Dependency Injection**: Implemented with **Dagger Hilt**.
- 🚀 **Efficient Networking**: Uses **Retrofit** and **Flow** for API calls.

---

## 📸 Screenshots

<div style="display: flex; justify-content: space-between;">

  <div style="text-align: center; width: 48%; margin-right: 4%;  margin-left: 4%">
    <img src="https://raw.githubusercontent.com/Mohd-Kaif/Demo-Project/main/docs/images/home_screen.png" width="100%" />
    <p><strong>Home Screen</strong></p>
  </div>

  <div style="text-align: center; width: 48%; margin-right: 4%">
    <img src="https://raw.githubusercontent.com/Mohd-Kaif/Demo-Project/main/docs/images/character_details_screen.png" width="100%" />
    <p><strong>Character Details</strong></p>
  </div>

  <div style="text-align: center; width: 48%; margin-right: 4%">
    <img src="https://raw.githubusercontent.com/Mohd-Kaif/Demo-Project/main/docs/images/share_screen.png" width="100%" />
    <p><strong>Character Details</strong></p>
  </div>
</div>

---

## 📂 Project Structure
    📂 app/  
    ├── 📂 data/  
    │   ├── CharacterData.kt  # Data class for character details
    │   ├── ResponseData.kt  # Data class for response details 
    │   ├── DataProvider.kt  # Provides dummy data for previews
    ├── 📂 di/  
    │   ├── AppModule.kt  # Provides dependencies like Retrofit instance
    ├── 📂 model/  
    │   ├── CharacterRepository.kt  # Fetches character data from API with pagination
    ├── 📂 network/  
    │   ├── StarWarsApi.kt  # Interface for api calls
    ├── 📂 view/  
    │   ├── 📂 navigation/  
    │   │   ├── StarWarsNavGraph.kt  # Handles app navigation 
    │   ├── 📂 screens/  
    │   │   ├── CharacterDetailsScreen.kt  # Displays character details 
    │   │   ├── HomeScreen.kt  # Displays character list 
    ├── 📂 viewModel/
    │   ├── HomeViewModel.kt  # Handles pagination logic and API calls
    ├── Constants.kt  
    ├── MainActivity.kt
    ├── StarWarsApp.kt
    ├── StarWarsApplication.kt

---

## 🛠 Tech Stack

- **UI**: Jetpack Compose, Material Design 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Networking**: Retrofit, OkHttp
- **Dependency Injection**: Dagger Hilt
- **Pagination**: Custom Pagination
- **State Management**: Flow, StateFlow
- **Navigation**: Jetpack Navigation

## 📦 Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/Mohd-Kaif/Demo-Project.git
2. Open the project in Android Studio
3. Sync Gradle and build the project
4. Run the app on an emulator or a physical device

