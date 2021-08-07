package common.compat.v0_5

import com.intellij.conversion.*
import database.AutoCpDB
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.file.Paths
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.readText

class AutoCpProjectConverterProvider : ConverterProvider() {
    override fun getConversionDescription(): String {
        return "Current Settings, .autocp and Run configurations will be removed and replaced with new defaults"
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

                return try {
                    Json.decodeFromString<AutoCpDB>(autoCpFile.readText())
                    false
                } catch (e: Exception) {
                    true
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
