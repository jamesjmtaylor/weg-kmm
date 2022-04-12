plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.jamesjmtaylor.weg.android"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
//    TODO: migrate remaining dependencies to dependencyResolutionManagement
dependencies {
    implementation(project(":shared"))
    implementation(libs.material)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.contraintlayout)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.activity)

    androidTestImplementation(libs.androidx.ui.test)
    debugImplementation(libs.androidx.ui.tooling)
}