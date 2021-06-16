package config

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import common.Constants

class AutoCpConfigFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return AutoCpConfig(project, this, Constants.FrameworkName)
    }

    override fun getId() = Constants.FrameworkId
}