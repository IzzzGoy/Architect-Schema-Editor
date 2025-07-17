import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.multiplatform)
}

kotlin {

    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        binaries {
            executable {
                mainClass ="ru.alexey.ndimmatrix.database.migrations.MainKt"
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.server.database.schema)

                implementation("com.uchuhimo:konf:1.1.2")

            }
        }
    }
}

tasks.forEach {
    println(it.name)
}