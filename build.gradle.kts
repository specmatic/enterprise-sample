import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.10" apply false
    kotlin("plugin.spring") version "2.3.10" apply false
    id("org.springframework.boot") version "3.2.6" apply false
    id("io.spring.dependency-management") version "1.1.5" apply false
    id("com.google.protobuf") version "0.9.6" apply false
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
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.add("-Xjsr305=strict")
        }
    }
}
