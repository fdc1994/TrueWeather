plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hiltLibrary)
    kotlin("kapt")
}

android {
    namespace = "com.example.trueweather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.trueweather"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures{
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.viewModel)
    implementation(libs.androidx.viewModelKtx)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.joda.datetime)
    implementation(libs.airbnb.lottie)
    implementation(project(":domain"))
    implementation(project(":data"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)
    implementation(libs.retrofit)
    implementation(libs.retrofitGSON)
    implementation(libs.rxJava)
    implementation(libs.adapterRxJava)
    implementation(libs.rxAndroid)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}