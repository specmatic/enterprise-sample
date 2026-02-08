rootProject.name = "enterprise-sample"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    ":common-proto",
    ":shared-contract",
    ":placeorder-service"
)
