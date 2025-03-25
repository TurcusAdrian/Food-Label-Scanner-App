plugins {

    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("kotlin-android")
}

android {
    namespace = "com.example.food_label_scanner"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.food_label_scanner"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }
    buildFeatures{
        viewBinding = true
    }

}

dependencies {

    //module for barcode scanning:
    implementation("com.google.mlkit:barcode-scanning:17.3.0")


    //modules for Dagger-Hilt used for ViewModel
    implementation("com.google.dagger:hilt-android:2.44")
    implementation(libs.volley)
    implementation(libs.androidx.media3.common.ktx)
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //Module used for storing pictures on the favourites screen:
    implementation("androidx.datastore:datastore-preferences:1.1.1")


    implementation("com.google.accompanist:accompanist-permissions:0.37.0")
    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation(libs.androidx.camera.core)
    implementation(libs.camera.camera2)
    // If you want to additionally use the CameraX Lifecycle library
    implementation(libs.androidx.camera.lifecycle)
    // If you want to additionally use the CameraX VideoCapture library
    implementation(libs.androidx.camera.video)
    // If you want to additionally use the CameraX View class
    implementation(libs.camera.view)
    // If you want to additionally add CameraX ML Kit Vision Integration
    implementation(libs.androidx.camera.mlkit.vision)
    // If you want to additionally use the CameraX Extensions library
    implementation(libs.androidx.camera.extensions)


    implementation(libs.text.recognition) // library for text recognition
    implementation(libs.androidx.navigation.compose) // library for navigation between screens
    implementation(libs.coil.kt.coil.compose) //library for images
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.camera.view)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}