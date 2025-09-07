import jdk.tools.jlink.resources.plugins

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.7.2"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

group = "com.example"
version = "1.0.0"
description = "Plugin de bloques temporales con WinterFramework"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Paper API
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")

    // Winter Framework
    api("com.thewinterframework:paper:1.0.0")
    annotationProcessor("com.thewinterframework:paper:1.0.0")

    // Configurate for advanced config handling
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("org.spongepowered:configurate-core:4.1.2")
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
}
