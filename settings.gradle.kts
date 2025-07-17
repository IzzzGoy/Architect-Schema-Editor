enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Schema-Editor"

pluginManagement {
    repositories {
        google {
            content { 
              	includeGroupByRegex("com\\.android.*")
              	includeGroupByRegex("com\\.google.*")
              	includeGroupByRegex("androidx.*")
              	includeGroupByRegex("android.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content { 
              	includeGroupByRegex("com\\.android.*")
              	includeGroupByRegex("com\\.google.*")
              	includeGroupByRegex("androidx.*")
              	includeGroupByRegex("android.*")
            }
        }
        mavenCentral()
    }
}
plugins {
    //https://github.com/JetBrains/compose-hot-reload?tab=readme-ov-file#set-up-automatic-provisioning-of-the-jetbrains-runtime-jbr-via-gradle
    id("org.gradle.toolchains.foojay-resolver-convention").version("0.10.0")
}

include(":composeApp")
include(":client:data:api")
include(":client:data:impl")
include(":client:ui")
include(":client:presentation:api")
include(":client:presentation:impl")
include(":client:uikit")
include(":client:usecases:api")
include(":client:usecases:impl")
include(":client:utils")
include(":api")
include(":server:database:schema")
include(":server:database:migrations")

