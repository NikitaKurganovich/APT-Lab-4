pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io" )
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io" )
    }
}

rootProject.name = "APT Lab 4"
include(":app")
 