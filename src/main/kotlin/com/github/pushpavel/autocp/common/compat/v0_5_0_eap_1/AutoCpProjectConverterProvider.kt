package com.github.pushpavel.autocp.common.compat.v0_5_0_eap_1

import com.intellij.conversion.*

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
        }
    }


}
