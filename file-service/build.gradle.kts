plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("kapt") version "1.9.0"
    kotlin("plugin.jpa") version "1.8.0"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.serialization") version "2.1.20"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring
    implementation("io.github.cdimascio:dotenv-java:2.2.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    // Metrics
    implementation("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // Kotlinx coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")

    // Kotlinx serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")

    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    // Mapper
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")

    // JWT Auth
    implementation("io.jsonwebtoken:jjwt:0.12.6")
    implementation("javax.xml.bind:jaxb-api:2.3.0")

    // Tests
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Utilities
    implementation("one.stayfocused.spring:dotenv-spring-boot:1.0.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
