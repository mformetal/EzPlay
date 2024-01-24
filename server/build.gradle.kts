plugins {
    alias(libs.plugins.kotlin.jvm)
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
    implementation(libs.logback)
    implementation(libs.songMetadataReader)
    implementation(libs.coroutines.jvm)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
