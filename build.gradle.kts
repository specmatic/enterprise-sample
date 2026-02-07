import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22" apply false
    kotlin("plugin.spring") version "1.9.22" apply false
    id("org.springframework.boot") version "3.2.6" apply false
    id("io.spring.dependency-management") version "1.1.5" apply false
    id("com.google.protobuf") version "0.9.4" apply false
}

group = "com.example"
version = "0.1.0"

val grpcVersion by extra("1.58.0")

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = freeCompilerArgs + listOf("-Xjsr305=strict")
        }
    }
}
