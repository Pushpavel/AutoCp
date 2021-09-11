package com.github.pushpavel.autocp.common.compat.v0_5_0_eap_1

import com.intellij.conversion.*
import com.github.pushpavel.autocp.database.AutoCpDB
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.file.Paths
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.readText

class AutoCpProjectConverterProvider : ConverterProvider() {
    override fun getConversionDescription(): String {
        return "AutoCp Settings, .autocp file and its Run configurations will be removed to use the new version"
    }

    override fun createConverter(context: ConversionContext): ProjectConverter {
        return object : ProjectConverter() {

            // Removes Run configurations with name autoCP
            override fun createRunConfigurationsConverter(): ConversionProcessor<RunManagerSettings> {
                return object : ConversionProcessor<RunManagerSettings>() {
                    override fun isConversionNeeded(settings: RunManagerSettings): Boolean {
                        return settings.runConfigurations.any { it.name == "autoCP" }
                    }

                    override fun process(settings: RunManagerSettings) {
                        settings.runConfigurations.removeIf { it.name == "autoCP" }
                    }
                }
            }


            // Does .autocp need to be deleted
            override fun isConversionNeeded(): Boolean {
                val autoCpFile = Paths.get(context.projectBaseDir.pathString, ".autocp")
                if (!autoCpFile.exists())
                    return false
                val text = autoCpFile.readText()

                return try {
                    Json.decodeFromString<AutoCpDB>(text)
                    false
                } catch (e: Exception) {
                    // ensures we are not deleting json file
                    text.firstOrNull() != '{'
                }
            }

            // Deletes .autocp
            override fun processingFinished() {
                val autoCpFile = Paths.get(context.projectBaseDir.pathString, ".autocp")
                autoCpFile.deleteIfExists()
            }
        }
    }


}
