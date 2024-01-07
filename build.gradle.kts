plugins {
    kotlin("jvm") version "1.9.20"
    id("maven-publish")
    kotlin("plugin.serialization") version "1.9.20"
}

group = "xyz.nietongxue"
version = "1.0-SNAPSHOT"
val serializationVersion = "1.6.0"

repositories {
    mavenCentral()
}

dependencies {

    implementation("app.softwork:kotlinx-uuid-core:0.0.22")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")


    implementation("com.fasterxml.jackson.core:jackson-core:2.15.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.3")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
    //uuid-creator
    implementation("io.arrow-kt:arrow-core:1.2.0")
    implementation("net.pearx.kasechange:kasechange-jvm:1.4.1")
    implementation("io.github.encryptorcode:pluralize:1.0.0")


    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-framework-engine:5.6.2")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.3.3")
    testImplementation("io.kotest:kotest-runner-junit5:5.6.2")}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
kotlin {
    jvmToolchain(19)
}