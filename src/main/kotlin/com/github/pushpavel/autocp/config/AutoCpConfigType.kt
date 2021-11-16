package com.github.pushpavel.autocp.config

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NotNullLazyValue
import com.github.pushpavel.autocp.common.res.R

/**
 * Definition of custom run configuration
 */
class AutoCpConfigType : ConfigurationTypeBase(
    id = R.strings.runConfigId,
    displayName = R.strings.runConfigName,
    description = R.strings.runConfigDescription,
    icon = NotNullLazyValue.createValue { R.icons.logo16 }
) {
    init {
        factory = AutoCpConfigFactory(this)
        addFactory(factory)
    }

    companion object {
        lateinit var factory: AutoCpConfigFactory
    }
}

/**
 * Factory class that creates new default run configurations
 */
class AutoCpConfigFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return AutoCpConfig(project, this, R.strings.runConfigName)
    }

    override fun getId() = R.strings.runConfigId
}