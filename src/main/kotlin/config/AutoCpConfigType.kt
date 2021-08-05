package config

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NotNullLazyValue
import common.Constants
import icons.Icons

/**
 * Definition of custom run configuration
 */
class AutoCpConfigType : ConfigurationTypeBase(
    id = Constants.RunConfigId,
    displayName = Constants.RunConfigName,
    description = Constants.RunConfigDescription,
    icon = NotNullLazyValue.createValue { Icons.LogoIcon }
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
        return AutoCpConfig(project, this, Constants.RunConfigName)
    }

    override fun getId() = Constants.RunConfigId
}