@file:Suppress("UnstableApiUsage")

pluginManagement {
    // build-logic disabled because it takes time every gradle sync to compile plugins
    // those plugin actually used only in 2 modules :core:base and :core:preferences
    // unfortunately github actions are pretty slow and each build slowed approx on 2 minutes
    // because of compiling build-logic folder
    // Enable it only if project really moves to KMP at least :data module
    //includeBuild("gradle/build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex(".*google.*")
                includeGroupByRegex(".*android.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()

        // Prerelease versions of Compose Multiplatform
        // maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

        // Used for snapshots if needed
        // maven("https://oss.sonatype.org/content/repositories/snapshots/")
        // maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}
dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex(".*google.*")
                includeGroupByRegex(".*android.*")
            }
        }
        mavenCentral()
        maven { url = uri("https://jitpack.io") }

        // Prerelease versions of Compose Multiplatform
        // maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

        // Used for snapshots if needed
        // maven("https://oss.sonatype.org/content/repositories/snapshots/")
        // maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
// https://docs.gradle.org/7.6/userguide/configuration_cache.html#config_cache:stable
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

rootProject.name = "rtm"

include(":app")