
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.ktor:ktor-server-caching-headers-jvm")

    implementation("io.ktor:ktor-server-locations:$ktor_version")


    implementation("org.jetbrains.exposed:exposed-core:0.49.0")
    implementation ("org.jetbrains.exposed:exposed-dao:0.49.0")
    implementation ("org.jetbrains.exposed:exposed-jdbc:0.49.0")
    implementation ("org.postgresql:postgresql:42.3.8")
    implementation ("com.zaxxer:HikariCP:4.0.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")

    implementation("io.ktor:ktor-server-openapi:$ktor_version")
    implementation("io.swagger.codegen.v3:swagger-codegen-generators:1.0.36")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")
}
