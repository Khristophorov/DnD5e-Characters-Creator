plugins {
    base
    kotlin("jvm") version "2.2.0" apply false
    kotlin("multiplatform") version "2.2.0" apply false
    kotlin("plugin.serialization") version "2.2.0" apply false
}

allprojects {
    group = "me.khris"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
