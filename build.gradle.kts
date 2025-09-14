plugins {
    java
    id("io.papermc.paperweight.userdev") version "1.7.2"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("com.gradleup.shadow") version "9.1.0"
}

group = "me.mapacheee"
version = "1.0.1"
description = "a plugin of temporal blocks"
base.archivesName.set("TemporalBlocks")

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")

    implementation("com.thewinterframework:paper:1.0.4")
    annotationProcessor("com.thewinterframework:paper:1.0.4")

    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("org.spongepowered:configurate-core:4.1.2")
}

configurations.all {
    resolutionStrategy {
        force("org.ow2.asm:asm:9.6")
        force("org.ow2.asm:asm-commons:9.6")
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()

        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to "1.21"
        )

        inputs.properties(props)

        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        archiveClassifier.set("")

        mergeServiceFiles()
        manifest {
            attributes(
                "Multi-Release" to "true"
            )
        }

        relocate("com.google.inject", "me.mapacheee.temporalblocks.libs.inject")
        relocate("org.spongepowered.configurate", "me.mapacheee.temporalblocks.libs.configurate")
    }

    build {
        dependsOn(shadowJar)
    }
}
