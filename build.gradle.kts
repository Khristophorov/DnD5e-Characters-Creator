@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    kotlin("multiplatform") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    application
}

group = "me.khris"
version = "1.0-SNAPSHOT"

// Kotlin dependencies
val kotlinCoroutinesVersion = "1.8.1"
val kotlinHtmlVersion = "0.11.0"
val kotlinReactVersion = "18.2.0-pre.638"
val kotlinMuiVersion = "5.14.12-pre.638"
val kotlinMuiIconsVersion = "5.14.12-pre.638"
val kotlinEmotionVersion = "11.11.1-pre.638"
val kotlinSerializationVersion = "1.6.3"
val kmongoVersion = "4.11.0"
val ktorVersion = "2.3.11"
val logbackVersion = "1.5.6"
val mockkVersion = "1.13.11"

// JavaScript dependencies
val reactMaterialUiFormValidatorVersion = "3.0.1"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
        withJava()
    }
    js(IR) {
        browser {
            binaries.executable()
            val enableCss = Action<KotlinWebpack> {
                cssSupport {
                    enabled.set(true)
                }
                mainOutputFileName.set("output.js")
            }
            webpackTask(enableCss)
            runTask(enableCss)
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport {
                        enabled.set(true)
                    }
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-server-sessions:$ktorVersion")
                implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
                implementation("io.ktor:ktor-server-auth:$ktorVersion")
                implementation("io.ktor:ktor-server-locations:$ktorVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-client-apache:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("org.litote.kmongo:kmongo:$kmongoVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinHtmlVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("io.mockk:mockk:$mockkVersion")
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:$kotlinReactVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:$kotlinReactVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-mui:$kotlinMuiVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-mui-icons:$kotlinMuiIconsVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:$kotlinEmotionVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation(npm("react-material-ui-form-validator", reactMaterialUiFormValidatorVersion))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack") {
    dependsOn("jsDevelopmentExecutableCompileSync")
    mainOutputFileName.set("output.js")
}

tasks.getByName<KotlinWebpack>("jsBrowserDevelopmentWebpack") {
    dependsOn("jsProductionExecutableCompileSync")
    mainOutputFileName.set("output.js")
}

tasks.getByName<Jar>("jvmJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    val taskName = if (project.hasProperty("isProduction")
        || project.gradle.startParameter.taskNames.contains("installDist")
    ) {
        "jsBrowserProductionWebpack"
    } else {
        "jsBrowserDevelopmentWebpack"
    }
    val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
    dependsOn(webpackTask)
    from(File(webpackTask.outputDirectory.asFile.get(), webpackTask.mainOutputFileName.get())) {
        into("assets")
    }
}

tasks.getByName<JavaExec>("run") {
    dependsOn(tasks.getByName<Jar>("jvmJar"))
    classpath(tasks.getByName<Jar>("jvmJar"))
}
