plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

val grpcVersion: String by rootProject.extra

dependencies {
    implementation(project(":shared-contract"))
    implementation(project(":common-proto"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("net.devh:grpc-client-spring-boot-starter:3.1.0.RELEASE")
    implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")
    runtimeOnly("io.grpc:grpc-netty-shaded:$grpcVersion")
    implementation("io.grpc:grpc-core:$grpcVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
