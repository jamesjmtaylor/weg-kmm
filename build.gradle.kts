buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${libs.versions.kotlin.get()}")
        classpath("com.squareup.sqldelight:gradle-plugin:${libs.versions.sqldelight.get()}")
        classpath("com.android.tools.build:gradle:7.2.2")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
    //Without this iOS throws:
    //"Uncaught Kotlin exception: kotlin.Error: Ktor native HttpClient requires kotlinx.coroutines version with `native-mt`"
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlinx:kotlinx-coroutines-core:${libs.versions.coroutines.get()}-native-mt")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}