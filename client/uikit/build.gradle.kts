plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvmToolchain(11)
    androidTarget {
        //https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
        //instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    jvm()


    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain{
            dependencies {
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)
            }
        }

        commonTest.dependencies {

        }

    }
}

android {
    namespace = "ru.alexey.ndimmatrix"
    compileSdk = 35

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
