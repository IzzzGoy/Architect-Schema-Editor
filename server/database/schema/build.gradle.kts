plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.buildConfig)
}

kotlin {
    jvmToolchain(11)

    jvm()


    sourceSets {
        jvmMain {
            dependencies {
                implementation(libs.exposed.core)
                implementation(libs.exposed.dao)
                implementation(libs.exposed.jdbc)

                implementation(libs.exposed.kotlin.datetime)
                implementation(libs.exposed.json)

                implementation("org.postgresql:postgresql:42.7.6")
                implementation("com.uchuhimo:konf:1.1.2")

            }
        }

        commonTest.dependencies {

        }

    }
}
