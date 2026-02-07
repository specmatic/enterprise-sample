rootProject.name = "order-system"

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
    ":inventory-service",
    ":placeorder-service"
)
