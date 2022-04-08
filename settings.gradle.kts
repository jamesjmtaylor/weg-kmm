//enableFeaturePreview("VERSION_CATALOGS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}
//    TODO: migrate remaining dependencies to dependencyResolutionManagement
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.6.0")
            version("ktor","1.6.8")
            library("coroutines","org.jetbrains.kotlinx:kotlinx-coroutines-core","coroutines").versionRef("kotlin")

            library("ktor-core","io.ktor","ktor-client-core").versionRef("ktor")
            library("ktor-json","io.ktor","ktor-client-json").versionRef("ktor")
            library("ktor-logging","io.ktor","ktor-client-logging").versionRef("ktor")
            library("ktor-serialization","io.ktor","ktor-client-serialization").versionRef("ktor")

        }
    }
}

rootProject.name = "Worldwide_Equipment_Guide"
include(":androidApp")
include(":shared")