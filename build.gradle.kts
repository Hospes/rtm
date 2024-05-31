import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

buildscript {
    dependencies {
        // Fixing
        // A failure occurred while executing dagger.hilt.android.plugin.task.AggregateDepsTask$WorkerAction
        //   > 'java.lang.String com.squareup.javapoet.ClassName.canonicalName()'
        classpath(libs.classpath.javapoet)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
}

allprojects {

    // Configure Java to use our chosen language level. Kotlin will automatically pick this up
    plugins.withType<JavaBasePlugin>().configureEach {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }

    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            // Treat all Kotlin warnings as errors
            //allWarningsAsErrors.set(true)

            // Enable experimental coroutines APIs, including Flow
            //freeCompilerArgs.addAll(
            //    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            //    "-opt-in=kotlinx.coroutines.FlowPreview",
            //)

            // To get compose metrics run this:
            // ./gradlew assembleDebug -Pmyapp.enableComposeCompilerReports=true
            if (project.hasProperty("myapp.enableComposeCompilerReports")) {
                val path = project.layout.buildDirectory.dir("compose_metrics").get().asFile.path
                freeCompilerArgs.addAll(
                    "-P", "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$path",
                )
                freeCompilerArgs.addAll(
                    "-P", "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$path",
                )
            }
        }
    }

    // Configure Android projects
    pluginManager.withPlugin("com.android.application") { configureAndroidProject() }
    pluginManager.withPlugin("com.android.library") { configureAndroidProject() }
    pluginManager.withPlugin("com.android.test") { configureAndroidProject() }
    pluginManager.withPlugin("org.jetbrains.kotlin.plugin.compose") { configureComposeProject() }
}

// Remove also build folder in root folder
tasks.register<Delete>("clean") {
    delete.add(rootProject.layout.buildDirectory)
}

fun Project.configureAndroidProject() {
    extensions.configure<BaseExtension> {
        compileSdkVersion(34)

        defaultConfig {
            minSdk = 23
            targetSdk = 34
        }

        // Can remove this once https://issuetracker.google.com/issues/260059413 is fixed.
        // See https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17

            // https://developer.android.com/studio/write/java8-support
            isCoreLibraryDesugaringEnabled = true
        }
    }

    dependencies {
        // https://developer.android.com/studio/write/java8-support
        "coreLibraryDesugaring"(libs.tools.desugarjdklibs)
    }
}

fun Project.configureComposeProject() {
    extensions.configure<ComposeCompilerGradlePluginExtension> {
        enableStrongSkippingMode = true
    }
}