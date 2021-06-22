package config

import com.google.common.io.Files
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.openapi.components.service
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import database.AcpDatabase
import settings.AutoCpSettings

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
        val solutionPath = context.location?.virtualFile?.path ?: return false
        val service = context.project.service<AcpDatabase>()
        val problem = service.getProblem(solutionPath)
        if (problem.isFailure) return false

        configuration.solutionFilePath = solutionPath
        configuration.solutionLangId = getSelectedLangId(solutionPath)

        val suggestedName = configuration.suggestedName()
        if (suggestedName != null)
            configuration.name = suggestedName

        return true
    }

    /**
     * Selecting a language for the [AutoCpConfig] created from Context
     * based on Preference and extension
     */
    private fun getSelectedLangId(solutionPath: String): Long {
        val extension = Files.getFileExtension(solutionPath)
        val settings = AutoCpSettings.instance
        // selecting solution Language

        // 1. Preferred Language from settings
        val prefLang = settings.getPreferredLang()
        if (prefLang != null && prefLang.extension == extension)
            return prefLang.id

        // 2. Any Solution Language with correct file extension
        val lang = settings.solutionLanguages.find { it.extension == extension }
        if (lang != null)
            return lang.id

        // 3. Any Solution Language
        val anyLang = settings.solutionLanguages.firstOrNull()

        if (anyLang != null)
            return anyLang.id

        // 4. no Language selected
        return -1
    }

    /**
     * Used to reuse existing AutoCpConfig created from this Context
     */
    override fun isConfigurationFromContext(configuration: AutoCpConfig, context: ConfigurationContext): Boolean {
        val path = context.location?.virtualFile?.path ?: return false
        return configuration.solutionFilePath == path
    }


    override fun getConfigurationFactory() = AutoCpConfigType.factory

}