import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.tasks.RunPluginVerifierTask.FailureLevel

fun properties(key: String) = project.findProperty(key).toString()

// Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
val pluginDescriptionFromMd = File(projectDir, "README.md").readText().lines().run {
    val start = "<!-- Plugin description -->"
    val end = "<!-- Plugin description end -->"

    if (!containsAll(listOf(start, end))) {
        throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
    }
    subList(indexOf(start) + 1, indexOf(end))
}.joinToString("\n").run { markdownToHTML(this) }


plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij") version "1.9.0"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "1.3.1"

    kotlin("plugin.serialization") version "1.7.20"
}

group = properties("pluginGroup")
version = properties("pluginVersion")
description = pluginDescriptionFromMd

// Configure project's dependencies
repositories {
    mavenCentral()
}

// Set the JVM language level used to compile sources and generate files - Java 11 is required since 2020.3
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    val ktor_version = "2.1.2"

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-java:$ktor_version")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("io.mockk:mockk:1.13.2")
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
    instrumentCode.set(false)
}

// Configure gradle-changelog-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    version.set(properties("pluginVersion"))
    header.set(provider { version.get() })
    groups.set(emptyList())
    headerParserRegex.set(Regex("""v[0-9]+\.[0-9]+\.[0-9]+(-eap\.[1-9]+)?"""))
}

tasks {

    buildSearchableOptions {
        enabled = false
    }

    test {
        useJUnitPlatform()
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    patchPluginXml {
        pluginId.set(properties("pluginGroup"))
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))
        pluginDescription.set(pluginDescriptionFromMd)

        // Get the latest available change notes from CHANGELOG.md
        changeNotes.set(provider { changelog.getLatest().toHTML() })
    }

    runPluginVerifier {
        val failLevel = FailureLevel.ALL.clone()
        failLevel.remove(FailureLevel.EXPERIMENTAL_API_USAGES)
        failLevel.remove(FailureLevel.DEPRECATED_API_USAGES)
        failureLevel.set(failLevel)
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set(System.getenv("PUBLISH_TOKEN"))
        // pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        if (properties("pluginVersion").contains('-'))
            channels.set(listOf("eap"))
        else
            channels.set(listOf("default", "eap"))
    }

    runIde {
        // workaround for https://stackoverflow.com/questions/60027717/intellij-idea-vm-options
        jvmArgs(
            "-XX:+UnlockDiagnosticVMOptions",
            "--illegal-access=deny",
            "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
            "--add-opens=java.desktop/java.awt=ALL-UNNAMED",
            "--add-opens=java.base/java.lang=ALL-UNNAMED",
            "--add-opens=java.desktop/javax.swing=ALL-UNNAMED",
            "--add-opens=java.desktop/javax.swing.plaf.basic=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.font=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.swing=ALL-UNNAMED",

            "--add-opens=java.desktop/java.awt.event=ALL-UNNAMED",
            "--add-opens=java.base/java.util=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED",
            "--add-opens=java.desktop/javax.swing.text.html=ALL-UNNAMED",
            "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",

            "--add-exports",
            "java.base/jdk.internal.vm=ALL-UNNAMED",
            "--add-exports",
            "java.desktop/sun.java2d=ALL-UNNAMED"
        )
    }
}
