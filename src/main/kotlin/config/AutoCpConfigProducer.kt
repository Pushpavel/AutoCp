package config

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.openapi.util.Ref
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import database.autoCpDatabase
import settings.langSettings.AutoCpLangSettings

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
        val solutionFile = context.location?.virtualFile
        val solutionPath = solutionFile?.path ?: return false
        val service = context.project.autoCpDatabase()
        val problem = service.getProblem(solutionPath)
        if (problem.isFailure) return false

        configuration.solutionFilePath = solutionPath
        configuration.buildConfigId = getSelectedBuildConfigId(solutionFile)

        val suggestedName = configuration.suggestedName()
        if (suggestedName != null)
            configuration.name = suggestedName

        return true
    }

    /**
     * Selecting a BuildConfig for the [AutoCpConfig] created from Context
     * based on Preference and File's Language
     */
    private fun getSelectedBuildConfigId(solutionFile: VirtualFile): Long {
        val lang = AutoCpLangSettings.findLangByFile(solutionFile) ?: return -1

        // TODO: select default BuildConfig for a Language

        return lang.buildConfigs.takeIf { it.isNotEmpty() }?.get(0)?.id ?: -1
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