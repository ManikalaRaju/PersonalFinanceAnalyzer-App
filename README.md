# Personal Finance Analyzer 📊

An intuitive Android app designed to help users manage and analyze their daily expenses, visualize spending trends, and sync data securely using Firebase and Room.

---

## 💡 App Overview

**Personal Finance Analyzer** allows users to:
- Log daily expenses with category, amount, and notes
- Visualize spending via charts (MPAndroidChart)
- Switch between light and dark mode
- Store data locally (Room) and sync with Firestore
- Secure login/signup via Firebase Authentication
- Offline support with auto-sync when online

---

## 👨‍💼 Target Audience

- University students tracking monthly expenses
- Budget-conscious individuals managing bills
- Users seeking privacy-first, ad-free expense tracking

---

## 🧩 Core Features

| Feature                     | Description |
|----------------------------|-------------|
| 🔐 Authentication          | Secure login and signup via FirebaseAuth |
| ✏️ Expense Entry            | Add/edit/delete expenses with category and note |
| 📈 Reports & Charts         | View pie & bar charts of spending trends |
| 🌐 Cloud Sync               | Firestore sync for persistent, cross-device access |
| 📴 Offline Mode             | Uses Room to cache expenses locally and sync later |
| 🌙 Dark Mode                | Toggle dark/light UI theme from the drawer |
| 👤 User Isolation           | Data partitioned per user UID in Firestore |
| 🔄 Auto Refresh             | Syncs fresh data from Firestore after login |
| ✅ Fully Offline-Tested     | Functions without internet and syncs when back online |

---

## ⚙️ Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM
- **Database**: Room (local), Firebase Firestore (cloud)
- **Authentication**: FirebaseAuth
- **Charts**: MPAndroidChart (Bar & Pie)
- **Navigation**: Jetpack Navigation Component
- **UI**: Jetpack Compose + Material 3

---

## 🔒 Security & Privacy

- User data stored per UID (`/users/{uid}/expenses`)
- Local Room DB is wiped on logout
- App uses FirebaseAuth for authentication — passwords not stored locally
- No ads, no third-party analytics, no data sharing

---

## 📁 Folder Structure (Main Components)
app/
├── src/
│   └── main/
│       ├── java/
│       │   └── uk/ac/tees/mad/s3470478/
│       │       ├── model/
│       │       │   ├── Expense.kt
│       │       │   ├── ExpenseEntity.kt
│       │       │   ├── ExpenseDao.kt
│       │       │   └── ExpenseDatabase.kt
│       │       │
│       │       ├── viewmodel/
│       │       │   ├── ExpenseViewModel.kt
│       │       │   ├── ExpenseViewModelFactory.kt
│       │       │   ├── AuthUiState.kt
│       │       │   └── AuthenticationViewModel.kt
│       │       │
│       │       ├── utils/
│       │       │   ├── ExpenseFirestoreHelper.kt
│       │       │   ├── ReceiptAnalyzer.kt
│       │       │   └── getCategoryIcon.kt
│       │       │
│       │       ├── ui/theme
│       │       │   ├── Color.kt
│       │       │   ├── Theme.kt
│       │       │   ├── Type.kt
│       │       │
│       │       ├── HomeScreen.kt
│       │       ├── AddExpenseScreen.kt
│       │       ├── EditExpenseScreen.kt
│       │       ├── ReportsScreen.kt
│       │       ├── SplashScreen.kt
│       │       ├── LoginScreen.kt
│       │       ├── SignupScreen.kt
│       │       ├── ProfileScreen.kt
│       │       ├── CameraScreen.kt
│       │       ├── AppNavHost.kt
│       │       ├── BottomNavigationBar.kt
│       │       ├── BottomBar.kt
│       │       └── MainActivity.kt
│       │
│       └── res/
│           ├── mipmap/
│           ├── raw/
│           ├── xml/
│           ├── values/
│           │   ├── colors.xml
│           │   ├── themes.xml
│           │   └── strings.xml
│           └── drawable/
│
├── build.gradle (app)
├── build.gradle (project)
└── AndroidManifest.xml


---

## 🧠 Legal & Ethical

- No unnecessary data collection
- GDPR-compliant structure
- Data is not shared or sold
- Users can delete data anytime via the app

---

## 👨‍🎓 Author

- **Name**: Raju Manikala
- **Student ID**: S3470478
- **Course**: MSc Computer Science (Advanced Practice) - Teesside University
- **Module**: CIS4034-N - Mobile App Development
