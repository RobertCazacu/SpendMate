plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.readwhritenotification"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.readwhritenotification"
        minSdk = 24
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

android {
    viewBinding {
        enable = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Alte dependențe existente...

    // Google Play Services
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // OkHttp pentru cereri HTTP
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")

    // JSON parsing
    implementation("org.json:json:20200518")

    //...
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0") // sau versiunea care se potrivește cu versiunea OkHttp

    //Dependenta principala pentru Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    //Convertor care permite conversia JSON-ului primit în obiecte Kotlin
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

}
