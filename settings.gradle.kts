//enableFeaturePreview("VERSION_CATALOGS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {

            version("kotlin", "1.7.10")
            version("coroutines", "1.6.0")
            version("kodein", "7.10.0")
            version("lifecycle", "2.2.0")
            version("sqldelight", "1.5.3")
            version("ktor", "1.6.8")
            version("compose", "1.3.1")
            version("navigation", "2.5.1")
            version("paging", "3.1.1")

            library("coroutines","org.jetbrains.kotlinx","kotlinx-coroutines-core").versionRef("coroutines")
            library("kotlin-stdlib", "org.jetbrains.kotlin", "kotlin-stdlib").versionRef("kotlin")
            library("kotlin-stdlib-common", "org.jetbrains.kotlin", "kotlin-stdlib-common").versionRef("kotlin")

            library("kodein", "org.kodein.di", "kodein-di").versionRef("kodein")
            library("lifecycle", "androidx.lifecycle", "lifecycle-extensions").versionRef("lifecycle")

            library("sqldelight", "com.squareup.sqldelight", "runtime").versionRef("sqldelight")
            library("sqldelight-android", "com.squareup.sqldelight", "android-driver").versionRef("sqldelight")
            library("sqldelight-ios", "com.squareup.sqldelight", "native-driver").versionRef("sqldelight")

            library("ktor-android","io.ktor","ktor-client-android").versionRef("ktor")
            library("ktor-ios","io.ktor","ktor-client-ios").versionRef("ktor")
            library("ktor-core","io.ktor","ktor-client-core").versionRef("ktor")
            library("ktor-json","io.ktor","ktor-client-json").versionRef("ktor")
            library("ktor-logging","io.ktor","ktor-client-logging").versionRef("ktor")
            library("ktor-serialization","io.ktor","ktor-client-serialization").versionRef("ktor")
            library("kotlinx-serialization", "org.jetbrains.kotlinx","kotlinx-serialization-core").version("1.2.2")

            library("multiplatform-paging", "io.github.kuuuurt","multiplatform-paging").version("0.5.0")

            //Android specific
            library("material", "com.google.android.material", "material").version("1.5.0")
            library("androidx-appcompat", "androidx.appcompat", "appcompat").version("1.4.1")
            library("androidx-contraintlayout", "androidx.constraintlayout", "constraintlayout").version("2.1.3")

            library("androidx-navigation-common", "androidx.navigation", "navigation-common").versionRef("navigation")
            library("androidx-navigation-common-ktx", "androidx.navigation", "navigation-common-ktx").versionRef("navigation")
            library("androidx-navigation-compose", "androidx.navigation", "navigation-compose").versionRef("navigation")

            library("androidx-ui","androidx.compose.ui","ui").versionRef("compose")
            library("androidx-material","androidx.compose.material","material").versionRef("compose")
            library("androidx-ui-tooling-preview","androidx.compose.ui","ui-tooling-preview").versionRef("compose")
            library("androidx-ui-tooling","androidx.compose.ui","ui-tooling").versionRef("compose")
            library("androidx-ui-runtime", "androidx.compose.runtime", "runtime-livedata").versionRef("compose")

            library("androidx-lifecycle","androidx.lifecycle","lifecycle-runtime-ktx").version("2.4.1")
            library("androidx-activity","androidx.activity","activity-compose").version("1.4.0")

            library("androidx-ui-test","androidx.compose.ui","ui-test-junit4").versionRef("compose")
            library("androidx-paging", "androidx.paging","paging-runtime").versionRef("paging")
            library("androidx-compose-paging", "androidx.paging", "paging-compose").version("1.0.0-alpha16")
        }
    }
}

rootProject.name = "WorldwideEquipmentGuide"
include(":androidApp")
include(":shared")