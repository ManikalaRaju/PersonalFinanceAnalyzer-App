plugins {
        id("com.android.application")
        id("com.google.gms.google-services")
        id("org.jetbrains.kotlin.android")
        id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"

}

android {
    namespace = "uk.ac.tees.mad.s3470478"
    compileSdk = 35

    defaultConfig {
        applicationId = "uk.ac.tees.mad.s3470478"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
    dependencies {
        // Core
        implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
        implementation("com.google.firebase:firebase-analytics")
        implementation("androidx.core:core-ktx:1.10.1")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
        implementation("androidx.activity:activity-compose:1.8.0")

        // Jetpack Compose BOM
        implementation("androidx.compose.compiler:compiler:1.5.11")
        implementation(platform("androidx.compose:compose-bom:2024.02.00"))
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.compose.material:material")
        implementation("androidx.compose.material3:material3")
        implementation("androidx.navigation:navigation-compose:2.7.7")

        // Firebase, Camera, OCR
        implementation("com.google.firebase:firebase-auth:22.3.1")
        implementation("com.airbnb.android:lottie-compose:6.2.0")
        implementation("androidx.compose.material:material-icons-extended")
        implementation("androidx.camera:camera-camera2:1.4.2")
        implementation("androidx.camera:camera-lifecycle:1.4.2")
        implementation("androidx.camera:camera-view:1.4.2")
        implementation("androidx.camera:camera-core:1.4.2")
        implementation("com.google.mlkit:text-recognition:16.0.0")
        implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")

        // MPAndroidChart for Reports
        implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

        // Room
        implementation("androidx.room:room-runtime:2.6.1")
        implementation("androidx.room:room-ktx:2.6.1")
        kapt("androidx.room:room-compiler:2.6.1")

        // Lifecycle & ViewModel
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

        // Debug/Test
        debugImplementation("androidx.compose.ui:ui-tooling")
        debugImplementation("androidx.compose.ui:ui-test-manifest")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))

}

kapt {
    correctErrorTypes = true
}