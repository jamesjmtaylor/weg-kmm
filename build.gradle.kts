buildscript {
    val compose_version by extra("1.1.1")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
//    TODO: migrate remaining dependencies to dependencyResolutionManagement
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.android.tools.build:gradle:7.1.3")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}