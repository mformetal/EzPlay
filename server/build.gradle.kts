plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor.server)
}

group = "com.ezplay"
version = "0.0.1"

application {
    mainClass.set("com.ezplay.ApplicationKt")

    applicationDefaultJvmArgs += listOf("-Dio.ktor.development=true")
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
    implementation(libs.common.ktor.serialization)
    implementation("io.ktor:ktor-server-partial-content")
    implementation(libs.kotlinx.serialization)
    implementation(libs.logback)
    implementation(libs.songMetadataReader)
    implementation(libs.jvm.coroutines)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
    implementation("io.ktor:ktor-server-partial-content-jvm")
    implementation("io.ktor:ktor-server-double-receive")

    implementation(projects.multiplatform.dto)

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

tasks.create("rerun") {
    dependsOn("clean", "run")
}
