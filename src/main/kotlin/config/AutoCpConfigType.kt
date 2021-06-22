package config

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NotNullLazyValue
import common.Constants

/**
 * Definition of custom run configuration
 */
class AutoCpConfigType : ConfigurationTypeBase(
    id = Constants.FrameworkId,
    displayName = Constants.FrameworkName,
    description = Constants.Description,
    icon = NotNullLazyValue.createValue { AllIcons.General.Modified }
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
        return AutoCpConfig(project, this, Constants.FrameworkName)
    }

    override fun getId() = Constants.FrameworkId
}