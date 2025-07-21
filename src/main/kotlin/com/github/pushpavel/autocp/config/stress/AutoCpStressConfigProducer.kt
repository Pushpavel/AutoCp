package com.github.pushpavel.autocp.config.stress

import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.database.SolutionFiles
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement

class AutoCpStressConfigProducer : LazyRunConfigurationProducer<AutoCpStressConfig>() {

    override fun setupConfigurationFromContext(
        configuration: AutoCpStressConfig,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val file = context.location?.virtualFile
        val solutionPath = file?.pathString ?: return false
        val solutionFiles = SolutionFiles.getInstance(context.project)

        if (solutionPath !in solutionFiles)
            return false

        configuration.solutionFilePath = solutionPath

        val suggestedName = configuration.suggestedName()
        if (suggestedName != null)
            configuration.name = suggestedName

        return true
    }

    override fun isConfigurationFromContext(
        configuration: AutoCpStressConfig,
        context: ConfigurationContext
    ): Boolean {
        val path = context.location?.virtualFile?.pathString ?: return false
        return configuration.solutionFilePath == path
    }

    override fun getConfigurationFactory() = AutoCpStressConfigType.factory

}
