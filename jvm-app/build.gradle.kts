plugins {
    kotlin("jvm") version "2.2.0"
    application
}

group = "me.khris"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktorVersion = "3.2.2"
val logbackVersion = "1.5.18"

dependencies {
    implementation(project(":kmp-lib"))

    // Add Ktor dependencies directly to ensure they're available at compile time
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Add Kotlin standard library
    implementation(kotlin("stdlib"))
}

application {
    mainClass.set("me.khris.dnd.charcreator.ApplicationKt")
}

// Configure Kotlin source directories
sourceSets {
    main {
        kotlin {
            srcDir("src/main/kotlin")
        }
    }
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // Include the JS output from the kmp-lib project
    dependsOn(":kmp-lib:jsBrowserProductionWebpack")

    from(project(":kmp-lib").layout.buildDirectory.file("js/packages/kmp-lib/kotlin/output.js")) {
        into("assets")
    }
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.jar)
    classpath(tasks.jar)
}
