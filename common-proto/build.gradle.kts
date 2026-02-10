import com.google.protobuf.gradle.*

plugins {
    id("java-library")
    id("com.google.protobuf")
}

val grpcVersion: String by rootProject.extra

dependencies {
    api("com.google.protobuf:protobuf-java:4.33.5")
    api("io.grpc:grpc-protobuf:$grpcVersion")
    api("io.grpc:grpc-stub:$grpcVersion")
    api("javax.annotation:javax.annotation-api:1.3.2")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.33.5"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
    }
    generateProtoTasks {
        all().configureEach {
            plugins {
                id("grpc")
            }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
