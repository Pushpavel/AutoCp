package config

import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.SimpleConfigurationType
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NotNullLazyValue
import common.Constants

class AutoCpConfigType : SimpleConfigurationType(
    id = Constants.FrameworkId,
    name = Constants.FrameworkName,
    description = Constants.Description,
    icon = NotNullLazyValue.createValue { AllIcons.General.Modified }
) {

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return AutoCpConfig(project, this, Constants.FrameworkName)
    }
}