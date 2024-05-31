plugins {
    kotlin("multiplatform") // id("com.whoppah.kotlin.multiplatform")
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinx.coroutines.core)
            }
        }

        jvmMain
    }
}

task("testClasses").doLast {
    println("This is a dummy testClasses task needed to build this module on Android Studio Iguana and higher")
}