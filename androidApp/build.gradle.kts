@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "com.jamesjmtaylor.weg.android"
        minSdk = 26
        targetSdk = 33
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
        freeCompilerArgs = listOf("-Xjvm-default=compatibility")
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
    namespace = "com.jamesjmtaylor.weg.android"
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.material)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.contraintlayout)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.ui.runtime)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.navigation.common)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.compose)
    //Used for paginating infinite scroll
    implementation(libs.androidx.paging)
    implementation(libs.androidx.compose.paging)
    //Used for paging through images
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.androidx.compose.constraintlayout)
    //NOTE: Using the settings.gradle.ks file for these dependencies causes a 404.  This is probably
    //because unlike the other dependencies they have a "-" in the group (text before the first ":")
    implementation("io.coil-kt:coil:2.5.0")
    implementation("io.coil-kt:coil-compose:2.5.0")

    androidTestImplementation(libs.androidx.ui.test)
    debugImplementation(libs.androidx.ui.tooling)
}