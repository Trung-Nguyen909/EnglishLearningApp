pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

// --- SỬA Ở KHỐI NÀY ---
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // ---> THÊM DÒNG NÀY VÀO ĐÂY <---
        maven { url = uri("https://jitpack.io") }
        // --------------------------------
    }
}
// ----------------------

rootProject.name = "EnglishLearningApp"
include(":app")