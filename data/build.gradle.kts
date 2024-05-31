plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ua.hospes.rtm.data"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

configurations {
    implementation {
        exclude(group = "androidx.activity")
        exclude(group = "androidx.fragment")
        exclude(group = "androidx.lifecycle")
        exclude(group = "androidx.savedstate")
        exclude(group = "androidx.legacy")
    }
}

dependencies {
    implementation(projects.core.base)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.google.hilt.android)
    ksp(libs.google.hilt.compiler)

    implementation(libs.timber)
}