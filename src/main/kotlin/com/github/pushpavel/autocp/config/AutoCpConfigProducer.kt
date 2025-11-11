package com.github.pushpavel.autocp.config

import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.database.SolutionFiles
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement

/**
 * Implementation class for creating [AutoCpConfig] from context
 */
class AutoCpConfigProducer : LazyRunConfigurationProducer<AutoCpConfig>() {

    /**
     * Sets up [AutoCpConfig] if the file in this context is associated with a Problem
     */
    private val log = Logger.getInstance(AutoCpConfigProducer::class.java)

    override fun setupConfigurationFromContext(
        configuration: AutoCpConfig,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        log.info("=== AutoCp: setupConfigurationFromContext called ===")
        val file = context.location?.virtualFile
        val solutionPath = file?.pathString ?: return false
        val solutionFiles = SolutionFiles.getInstance(context.project)

        log.info("AutoCp: File path = $solutionPath")
        log.info("AutoCp: File extension = ${file.extension}")

        if (solutionPath !in solutionFiles) {
            log.info("AutoCp: Returning FALSE - not in database")
            return false
        }

        configuration.solutionFilePath = solutionPath

        val suggestedName = configuration.suggestedName()
        if (suggestedName != null)
            configuration.name = suggestedName

        // 新增：记录配置详情
        log.warn("AutoCp: Configuration created successfully")
        log.warn("AutoCp: Config name = ${configuration.name}")
        log.warn("AutoCp: Config type = ${configuration.type.displayName}")
        log.warn("AutoCp: Config factory = ${configuration.factory}")

        log.info("AutoCp: Returning TRUE")
        return true
    }

    /**
     * Used to reuse existing AutoCpConfig created from this Context
     */
    override fun isConfigurationFromContext(configuration: AutoCpConfig, context: ConfigurationContext): Boolean {
        log.info("=== AutoCp: isConfigurationFromContext called ===")
        val path = context.location?.virtualFile?.pathString ?: return false
        val result = configuration.solutionFilePath == path
        log.info("AutoCp: Config path = ${configuration.solutionFilePath}")
        log.info("AutoCp: Context path = $path")
        log.info("AutoCp: isConfigurationFromContext returning = $result")

        return configuration.solutionFilePath == path
    }


    override fun getConfigurationFactory() = AutoCpConfigType.factory

}