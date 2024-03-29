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
    testImplementation("io.kotest:kotest-runner-junit5:5.6.2")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    //repository 负责说明发布到哪里
    //publication 负责说明发布什么
    //task会生成多个，两者的笛卡尔集。
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/nielinjie/kt-common")
            credentials {
                //properties in file://~/.gradle/gradle.properties
                username = project.findProperty("gprUser") as String? ?: System.getenv("GPRUSER")
                password = project.findProperty("gprToken") as String? ?: System.getenv("GPRTOKEN")
            }

        }
        maven {
            name = "AliMaven"
            credentials {
                username = project.findProperty("aliMUser") as String? ?: System.getenv("ALIMUSER")
                password = project.findProperty("aliMPass") as String? ?: System.getenv("ALIMPASS")
            }
            url = uri( "https://packages.aliyun.com/maven/repository/2331954-snapshot-Z6hK35/")
        }

    }

    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
kotlin {
    jvmToolchain(19)
}