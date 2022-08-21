import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    kotlin("multiplatform") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    application
}

group = "me.khris"
version = "1.0-SNAPSHOT"

// Kotlin dependencies
val kotlinCoroutinesVersion = "1.6.4"
val kotlinHtmlVersion = "0.8.0"
val kotlinReactVersion = "17.0.2-pre.290-kotlin-1.6.10"
val kotlinStyledVersion = "5.3.3-pre.290-kotlin-1.6.10"
val kotlinCssJsVersion = "1.0.0-pre.290-kotlin-1.6.10"
val kotlinSerializationVersion = "1.3.3"
val kmongoVersion = "4.7.0"
val ktorVersion = "2.1.0"
val slf4jVersion = "1.7.36"
val muirwikVersion = "0.10.1"
val mockkVersion = "1.12.5"

// JavaScript dependencies
val reactVersion = "17.0.2"
val reactMaterialUiFormValidatorVersion = "3.0.1"
val materialUiVersion = "5.3.0"
val emotionReactVersion = "11.4.1"
val emotionStyledVersion = "11.3.0"

repositories {
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
        withJava()
    }
    js(LEGACY) {
        browser {
            binaries.executable()
            webpackTask {
                cssSupport.enabled = true
            }
            runTask {
                cssSupport.enabled = true
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
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
                implementation("org.litote.kmongo:kmongo:$kmongoVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinHtmlVersion")
                implementation("org.slf4j:slf4j-simple:$slf4jVersion")
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
                implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:$kotlinStyledVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-css-js:$kotlinCssJsVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationVersion")
                implementation("com.ccfraser.muirwik:muirwik-components:$muirwikVersion")
                implementation(npm("@mui/material", materialUiVersion))
                implementation(npm("@mui/icons-material", materialUiVersion))
                implementation(npm("@mui/styled-engine-sc", materialUiVersion))
                implementation(npm("@emotion/react", emotionReactVersion))
                implementation(npm("@emotion/styled", emotionStyledVersion))
                implementation(npm("react", reactVersion))
                implementation(npm("react-dom", reactVersion))
                implementation(npm("react-is", reactVersion))
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
    outputFileName = "output.js"
}

tasks.getByName<Jar>("jvmJar") {
    dependsOn(tasks.getByName("jsBrowserProductionWebpack"))
    val jsBrowserProductionWebpack = tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack")
    from(File(jsBrowserProductionWebpack.destinationDirectory, jsBrowserProductionWebpack.outputFileName))
}

tasks.getByName<JavaExec>("run") {
    dependsOn(tasks.getByName<Jar>("jvmJar"))
    classpath(tasks.getByName<Jar>("jvmJar"))
}
