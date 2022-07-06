plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

version = "1.0"


kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "The WEG Shared Module provides a common data repository for equipment retrieval and storage."
        homepage = "https://jjmtaylor.com"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            //See https://github.com/cashapp/sqldelight/issues/2512#issuecomment-937699879
            //NOTE: If you get `dyld: Library not loaded` try running `pod install` from ./iosApp/
            isStatic = false
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlin.stdlib.common)

                implementation(libs.coroutines)
                implementation(libs.kodein)
                implementation(libs.sqldelight)

                // KTOR
                implementation(libs.ktor.core)
                implementation(libs.ktor.json)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.serialization)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlin.stdlib.common)
                implementation(libs.lifecycle)
                implementation(libs.sqldelight.android)

                // KTOR
                implementation(libs.ktor.android)
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
                implementation(libs.kotlin.stdlib.common)
                implementation(libs.sqldelight.ios)

                // KTOR
                implementation(libs.ktor.ios)
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

sqldelight {
    database("AppDatabase") {
        packageName = "com.jamesjmtaylor.weg.shared.cache"
    }
    linkSqlite = true //see https://github.com/cashapp/sqldelight/issues/1442#issuecomment-893978530
}