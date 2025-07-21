import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
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
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.foundation)
                implementation(compose.material3AdaptiveNavigationSuite)
                implementation(compose.materialIconsExtended)
                implementation(compose.components.resources)
                implementation(projects.client.uikit)

                implementation(libs.koin.compose)
                api(libs.koin.annotations)

                implementation(projects.client.presentation.api)
                implementation(projects.client.data.api)
                api(libs.ndimmartrix)

                implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.0-beta03")
                implementation("net.engawapg.lib:zoomable:2.8.1")
                implementation("com.darkrockstudios:mpfilepicker:3.1.0")
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

dependencies {
    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
}

project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}