plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
}

group = "app.group"

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    jvm("desktop")

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.material)
                implementation(compose.components.resources)
            }
        }
    }
}

android {
    compileSdk = 34
    namespace = "org.jetbrains.compose.resources.test"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        applicationId = "org.example.project"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    signingConfigs {
        create("testkey") {
            storeFile = project.file("key/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("testkey")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("testkey")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
