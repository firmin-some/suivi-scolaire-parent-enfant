plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "bf.ujkz.suiviscolaireparent"
    compileSdk {
        version = release(37) {
        }
    }

    defaultConfig {
        applicationId = "bf.ujkz.suiviscolaireparent"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    debugImplementation(libs.androidx.compose.ui.tooling)
    dependencies {
        // ... dépendances existantes (androidx.core, appcompat, material, etc.)

        // Réseau : Retrofit + conversion JSON
        implementation("com.squareup.retrofit2:retrofit:2.11.0")
        implementation("com.squareup.retrofit2:converter-gson:2.11.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

        // Coroutines (appels asynchrones)
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

        // ViewModel et LiveData
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")

        // Navigation entre écrans Compose
        implementation("androidx.navigation:navigation-compose:2.8.0")

        // Chargement des photos (équivalent Glide, mais natif Compose)
        implementation("io.coil-kt:coil-compose:2.7.0")
        // Stockage local du token (DataStore)
        implementation("androidx.datastore:datastore-preferences:1.1.1")
}
}