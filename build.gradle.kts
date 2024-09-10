import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val serializationVersion = "1.6.3"


plugins {
    val kotlinVersion = "1.9.22"
    kotlin("multiplatform") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    application
    id("maven-publish")
}

group = "xyz.nietongxue"
version = "1.0"

repositories {
    mavenCentral()
}
kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
                devServer?.proxy = mutableMapOf("/api" to "http://localhost:9000/")
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {

                implementation("app.softwork:kotlinx-uuid-core:0.0.22")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")



                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

                implementation("io.arrow-kt:arrow-core:1.2.0")
                implementation("net.pearx.kasechange:kasechange:1.4.1")

                implementation("com.eygraber:uri-kmp:0.0.18")

            }
        }
        val commonTest by getting {
            dependencies {

                implementation("io.kotest:kotest-framework-engine:5.6.2")
                implementation("io.kotest.extensions:kotest-assertions-arrow:1.3.3")
            }
        }
        val jvmMain by getting {
            dependencies{

                implementation("com.fasterxml.jackson.core:jackson-core:2.15.3")
                implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
                implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.3")
                implementation("io.github.encryptorcode:pluralize:1.0.0")
                implementation("com.appmattus.crypto:cryptohash:0.10.1")

                implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")

            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5:5.6.2")
            }
        }
    }
}



publishing {
    //repository 负责说明发布到哪里
    //publication 负责说明发布什么
    //task会生成多个，两者的笛卡尔集。
    //TODO 没有完全对，目前只有publishJvmPublicationToRepsyRepository运行过了，可能publications定义跟kotlin mp 定义的几个publish有冲突。
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
            url = uri("https://packages.aliyun.com/maven/repository/2331954-snapshot-Z6hK35/")
        }
        maven {
            name= "repsy"
            credentials {
                username = project.findProperty("repsyUser") as String? ?: System.getenv("REPSYUSER")
                password = project.findProperty("repsyToken") as String? ?: System.getenv("REPSYPASS")
            }
            url =uri("https://repo.repsy.io/mvn/nielinjie/default")
        }

    }
    //TODO 没有完全对，目前只有publishJvmPublicationToRepsyRepository运行过了，可能publications定义跟kotlin mp 定义的几个publish有冲突。

//    publications {
//        register<MavenPublication>("java") {
//            from(components["java"])
//        }
//    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-Xjsr305=strict"
    }
}