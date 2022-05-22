import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"

    application
    `maven-publish`

    id("uk.co.lukestevens.plugins.release-helper") version "0.1.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
    mavenCentral()
}

group = "uk.co.lucystevens"
version = "0.0.1"

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    }
}

val integrationTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

val ktormVersion = "3.4.1"
val koinVersion= "3.1.6"
val dockerJavaVersion = "3.2.13"
val jacksonVersion = "2.13.1"

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.insert-koin:koin-core:$koinVersion")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // logback for logging
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("com.github.maricn:logback-slack-appender:1.6.1")

    // kotlinx serialisation
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    // acme4j
    implementation("org.shredzone.acme4j:acme4j-client:2.13")
    implementation("org.shredzone.acme4j:acme4j-utils:2.13")

    // testing
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.12.3")
}

application {
    mainClass.set("uk.co.lucystevens.wildcert.LauncherKt")
}

/**
 *  Tasks
 */

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes("Main-Class" to application.mainClass.get())
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
}

val integrationTest = task<Test>("integrationTest") {
    useJUnitPlatform()
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    outputs.upToDateWhen { false }
}