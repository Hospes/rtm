plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
    id("dagger.hilt.android.plugin")
}

val buildNumber = if (project.hasProperty("build.number")) project.ext.get("build.number") else "dev"

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "ua.hospes.rtm"
        minSdkVersion(28)
        targetSdkVersion(29)

        versionCode = 9
        versionName = "2.0.$buildNumber"

        //archivesBaseName = "rtm-v$versionName"

        multiDexEnabled = true

        buildConfigField("String", "NOTIFICATION_CHANNEL_RACE", "\"Race progress\"")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
                arguments["room.schemaLocation"] = "$projectDir/schemas".toString()
            }
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("../debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }

        maybeCreate("release")
        getByName("release") {
            storeFile = file("../release.keystore")
            storePassword = "sync753df"
            keyAlias = "rtm"
            keyPassword = ">fFRteQE6,^uDQ]a"
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            versionNameSuffix = "d"
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")

            firebaseAppDistribution {
                serviceCredentialsFile = "racing-time-manager-ad1811bea75d.json"
                releaseNotesFile = "release-notes"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions { jvmTarget = "1.8" }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.72")

    val coroutines = "1.3.8"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")

    val lifecycle = "2.3.0-alpha05"
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle")

    implementation("androidx.activity:activity-ktx:1.2.0-alpha06")
    implementation("androidx.fragment:fragment-ktx:1.3.0-alpha06")
    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta8")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.drawerlayout:drawerlayout:1.1.0")
    implementation("com.google.android.material:material:1.3.0-alpha01")

    // logging
    implementation("com.jakewharton.timber:timber:4.7.1")


    val room = "2.3.0-alpha01"
    implementation("androidx.room:room-runtime:$room")
    kapt("androidx.room:room-compiler:$room")
    implementation("androidx.room:room-ktx:$room")   // Kotlin Extensions and Coroutines support for Room

    //implementation "ua.hospes.undobutton:undobutton:0.2.0"

    val hilt = "2.28.3-alpha"
    implementation("com.google.dagger:hilt-android:$hilt")
    kapt("com.google.dagger:hilt-android-compiler:$hilt")
    val androidx_hilt = "1.0.0-alpha01"
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:$androidx_hilt")
    kapt("androidx.hilt:hilt-compiler:$androidx_hilt")

    implementation("net.sourceforge.jexcelapi:jxl:2.6.12")

    implementation("com.google.firebase:firebase-crashlytics:17.1.1")
    implementation("com.google.firebase:firebase-analytics:17.4.4")

    debugImplementation("com.facebook.stetho:stetho:1.5.1")
}

kapt { correctErrorTypes = true }

androidExtensions { isExperimental = true }

apply(plugin = "com.google.gms.google-services")