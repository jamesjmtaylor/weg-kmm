plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = "1.0"


kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val kotlin_version = "1.6.0"
        val androidx_lifecycle_version = "2.2.0"
        val coroutine_version = "1.6.0"
        val moko_mvvm_version = "0.12.0"
        val kodeinVersion = "7.10.0"
        val ktor_version = "1.6.8"
        val sql_delight_version = "1.5.3"

        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlin:kotlin-stdlib-common")
                // COROUTINES
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version")
                // MOKO - MVVM
                implementation("dev.icerock.moko:mvvm-core:$moko_mvvm_version")
                implementation("dev.icerock.moko:mvvm-livedata:$moko_mvvm_version")
                implementation("dev.icerock.moko:mvvm-state:$moko_mvvm_version")
                implementation("dev.icerock.moko:mvvm-livedata-resources:$moko_mvvm_version")
                // KODE IN
                implementation("org.kodein.di:kodein-di:$kodeinVersion")
                // KTOR
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-json:$ktor_version")
                implementation("io.ktor:ktor-client-logging:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")
                // SQL Delight
                implementation("com.squareup.sqldelight:runtime:$sql_delight_version")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("dev.icerock.moko:mvvm-test:$moko_mvvm_version")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common:$kotlin_version")
                // MOKO - MVVM
                implementation("dev.icerock.moko:mvvm-livedata-material:$moko_mvvm_version")
                implementation("dev.icerock.moko:mvvm-livedata-glide:$moko_mvvm_version")
                implementation("dev.icerock.moko:mvvm-livedata-swiperefresh:$moko_mvvm_version")
                implementation("dev.icerock.moko:mvvm-databinding:$moko_mvvm_version")
                implementation("dev.icerock.moko:mvvm-viewbinding:$moko_mvvm_version")
                implementation("androidx.lifecycle:lifecycle-extensions:$androidx_lifecycle_version")
                // KTOR
                implementation("io.ktor:ktor-client-android:$ktor_version")
                // SQL Delight
                implementation("com.squareup.sqldelight:android-driver:$sql_delight_version")
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common:$kotlin_version")
                // KTOR
                implementation("io.ktor:ktor-client-ios:$ktor_version")
                // SQL Delight
                implementation("com.squareup.sqldelight:native-driver:$sql_delight_version")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 26
        targetSdk = 32
    }
}