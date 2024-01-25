plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization.server)
    alias(libs.plugins.ktor.server)
}

group = "com.ezplay"
version = "0.0.1"

application {
    mainClass.set("com.ezplay.ApplicationKt")

    val isDevelopment: Boolean = !project.ext.has("release")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.io.path.ExperimentalPathApi"
        )
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.3.0")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-freemarker")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-partial-content")
    implementation(libs.kotlinx.serialization)
    implementation(libs.logback)
    implementation(libs.songMetadataReader)
    implementation(libs.coroutines.jvm)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
    implementation("io.ktor:ktor-server-partial-content-jvm:2.3.7")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

tasks.create("rerun") {
    dependsOn("clean", "run")
}
