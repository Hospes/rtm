@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms)
    alias(libs.plugins.google.hilt)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.appdistribution)
}

val githubVersionCode: Int = System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull() ?: 0

android {
    namespace = "ua.hospes.rtm"

    defaultConfig {
        applicationId = "ua.hospes.rtm"

        versionCode = githubVersionCode + 10  //10 it's base version code from which we use automatic number
        versionName = gitDescribe()
        versionNameSuffix = versionSuffix()

        //archivesBaseName = "rtm-v$versionName"

        multiDexEnabled = true

        buildConfigField("String", "NOTIFICATION_CHANNEL_RACE", "\"Race progress\"")

//        javaCompileOptions {
//            annotationProcessorOptions {
//                // arguments += ["room.incremental": "true"]
//                arguments += ["room.schemaLocation": "$projectDir/schemas".toString()]
//            }
//        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("../debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release") {
            storeFile = file("../release.keystore")
            storePassword = "sync753df"
            keyAlias = "rtm"
            keyPassword = ">fFRteQE6,^uDQ]a"
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            versionNameSuffix = "-DEBUG"
            signingConfig = signingConfigs.getByName("debug")

            buildConfigField("Boolean", "CRASH_REPORTING", "false")
        }

        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            firebaseAppDistribution {
                serviceCredentialsFile = "racing-time-manager-a0f79d64e4ac.json"
                releaseNotes = gitNotes(gitLatestTag())
            }
        }
    }

    flavorDimensions += "type"
    productFlavors {
        create("dev") {
            dimension = "type"
            applicationIdSuffix = ".dev"
        }

        create("prod") {
            dimension = "type"
        }
    }

    kotlinOptions {
        // For creation of default methods in interfaces
        freeCompilerArgs += "-Xjvm-default=all"
    }
}

ksp {
    arg("compose-destinations.useComposableVisibility", "true")
    arg("compose-destinations.generateNavGraphs", "false")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.bundles.androidx.compose)
    implementation(libs.androidx.navigation)

//    implementation "com.google.accompanist:accompanist-insets:${google.accompanist}"

    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)

    implementation(libs.androidx.core)
    implementation(libs.androidx.activity.compose)
    implementation("androidx.fragment:fragment-ktx:1.7.1")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("androidx.annotation:annotation:1.8.0")
    implementation("com.google.android.material:material:1.12.0")

    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.5.6")

    val room = "2.6.1"
    implementation("androidx.room:room-runtime:$room")
    ksp("androidx.room:room-compiler:$room")
    implementation("androidx.room:room-ktx:$room")

    implementation(libs.google.hilt.android)
    ksp(libs.google.hilt.compiler)
    implementation(libs.androidx.hilt.navigation)

    implementation("net.sourceforge.jexcelapi:jxl:2.6.12")

    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(libs.timber)
}


fun versionSuffix(): String {
    return providers.exec {
        commandLine("git", "branch", "--show-current")
    }.standardOutput.asText.map { if (it == """release/(.+)""") "-RC" else "" }.get()
}

fun gitDescribe(): String {
    return providers.exec {
        commandLine("git", "describe", "--tags", "--always")
    }.standardOutput.asText.map { it.split("\n").first().trim() }.get()
}

fun gitLatestTag(): String {
    return providers.exec {
        commandLine("git", "tag", "--sort=-committerdate")
    }.standardOutput.asText.map { it.split("\n").first().trim() }.get()
}

fun gitNotes(latestTag: String): String {
    return providers.exec {
        val source = if (latestTag.isNotEmpty()) "$latestTag..HEAD" else "--max-count=20"
        commandLine("git", "log", "--pretty=* %s (%an) [%h]", source)
    }.standardOutput.asText.get().trim()
}