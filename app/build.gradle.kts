plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.chaquo.python")
}

android {
    namespace = "com.example.chaquopyapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chaquopyapp"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            // abiFilters の中身も正しい書き方か確認してください
            abiFilters += "armeabi-v7a"
            abiFilters += "arm64-v8a"
        }

        flavorDimensions += "pyVersion"
        productFlavors {
            create("py311") { dimension = "pyVersion" }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

chaquopy {
    productFlavors {
        getByName("py311") { version = "3.11" }
    }

    defaultConfig {
        buildPython("C:\\Users\\enter\\AppData\\Local\\Programs\\Python\\Python311\\python.exe")

        pip {

            install("numpy")
            //install("requests==2.24.0")

            // requirements.txtからまとめてインストール
            //install("-r", "requirements.txt")

        }

    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}