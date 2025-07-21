package com.github.pushpavel.autocp.config.stress

import com.github.pushpavel.autocp.common.res.R
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NotNullLazyValue

class AutoCpStressConfigType: ConfigurationTypeBase(
    id = R.strings.runStressConfigId,
    displayName = R.strings.runStressConfigName,
    description = R.strings.runStressConfigDescription,
    icon = NotNullLazyValue.createValue { R.icons.logo16 }
) {
    init {
        factory = AutoCpStressConfigFactory(this)
        addFactory(factory)
    }

    companion object {
        lateinit var factory: AutoCpStressConfigFactory
    }
}

class AutoCpStressConfigFactory(type: ConfigurationType) : ConfigurationFactory(type) {

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return AutoCpStressConfig(project, this, R.strings.runStressConfigName)
    }

    override fun getId() = R.strings.runStressConfigId
}
