package com.github.pushpavel.autocp.config

import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.core.persistance.storable
import com.github.pushpavel.autocp.core.persistance.storables.solutions.Solutions
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement

/**
 * Implementation class for creating [AutoCpConfig] from context
 */
class AutoCpConfigProducer : LazyRunConfigurationProducer<AutoCpConfig>() {

    /**
     * Sets up [AutoCpConfig] if the file in this context is associated with a Problem
     */
    override fun setupConfigurationFromContext(
        configuration: AutoCpConfig,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val file = context.location?.virtualFile
        val solutionPath = file?.pathString ?: return false
        val solutions = context.project.storable<Solutions>()

        if (solutionPath !in solutions)
            return false

        configuration.solutionFilePath = solutionPath

        val suggestedName = configuration.suggestedName()
        configuration.name = suggestedName

        return true
    }

    /**
     * Used to reuse existing AutoCpConfig created from this Context
     */
    override fun isConfigurationFromContext(configuration: AutoCpConfig, context: ConfigurationContext): Boolean {
        val path = context.location?.virtualFile?.pathString ?: return false
        return configuration.solutionFilePath == path
    }


    override fun getConfigurationFactory() = AutoCpConfigType.factory

}