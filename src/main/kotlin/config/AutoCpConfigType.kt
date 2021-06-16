package config

import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.icons.AllIcons
import com.intellij.openapi.util.NotNullLazyValue
import common.Constants

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