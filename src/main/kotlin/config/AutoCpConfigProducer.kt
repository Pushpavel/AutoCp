package config

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.openapi.util.Ref
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import database.autoCp
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
        val file = context.location?.virtualFile
        val solutionPath = file?.path ?: return false
        val db = context.project.autoCp()

        if (!db.solutionFiles.containsKey(solutionPath))
            return false

        configuration.solutionFilePath = solutionPath
        configuration.buildConfigId = getBuildConfigId(file)

        val suggestedName = configuration.suggestedName()
        if (suggestedName != null)
            configuration.name = suggestedName

        return true
    }

    /**
     * Selecting a BuildConfig for the [AutoCpConfig] created from Context
     * based on Preference and File's Language
     */
    private fun getBuildConfigId(solutionFile: VirtualFile): Long? {
        val lang = AutoCpLangSettings.findLangByFile(solutionFile) ?: return null

        return lang.getBuildConfig()?.id
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