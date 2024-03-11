plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.dokka") version "1.9.0"
    `java-library`
    signing
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.3"
}

group = "io.github.seggan"
version = "0.1.0"
description = "A simple library for creating Slimefun addons in Kotlin."

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnlyApi("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnlyApi("com.github.Slimefun:Slimefun4:RC-36")
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.14.0")
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.14.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withSourcesJar()
}

signing {
    useGpgCmd()
}

centralPortal {
    pom {
        url = "https://github.com/Seggan/sf4k"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                name = "Seggan"
                email = "seggan21@gmail.com"
                organization = "N/A"
                organizationUrl = "https://github.com/Seggan"
            }
        }
        scm {
            connection = "scm:git:git://github.com/Seggan/sf4k.git"
            developerConnection = "scm:git:ssh://github.com:Seggan/sf4k.git"
            url = "https://github.com/Seggan/sf4k"
        }
    }
}