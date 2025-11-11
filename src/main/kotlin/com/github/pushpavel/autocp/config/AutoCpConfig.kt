package com.github.pushpavel.autocp.config

import com.github.pushpavel.autocp.database.SolutionFiles
import com.github.pushpavel.autocp.database.autoCp
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializer
import org.jdom.Element
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension
import com.intellij.openapi.diagnostic.Logger
import com.intellij.execution.ExecutionTarget

/**
 * Implementation Class for a Custom Run Configuration that can also be created from context (by right-clicking and run)
 */
open class AutoCpConfig(project: Project, factory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase<RunProfileState>(project, factory, name) {
    companion object {
        private val LOG = Logger.getInstance(AutoCpConfig::class.java)
    }

    var solutionFilePath: String = ""

    private val solutionFiles = SolutionFiles.getInstance(project)

    /**
     * Returns [RunProfileState] that defines the execution of this Run Configuration
     */
    override fun getState(executor: Executor, environment: ExecutionEnvironment) : RunProfileState {
        LOG.warn("AutoCp Debug: getState() called")
        LOG.warn("AutoCp Debug: Executor = ${executor.id}")
        LOG.warn("AutoCp Debug: Solution file path = $solutionFilePath")
        LOG.warn("AutoCp Debug: Config name = $name")
        return AutoCpRunState(project, this)
    }

    override fun checkConfiguration() {
        LOG.warn("AutoCp Debug: checkConfiguration() called")
        LOG.warn("AutoCp Debug: Solution file path = $solutionFilePath")
        try {
            super.checkConfiguration()
            LOG.warn("AutoCp Debug: checkConfiguration() passed")
        } catch (e: Exception) {
            LOG.warn("AutoCp Debug: checkConfiguration() failed: ${e.message}")
            throw e
        }
    }

    override fun canRunOn(target: ExecutionTarget): Boolean {
        LOG.warn("AutoCp Debug: canRunOn(target) called with: ${target.displayName}")
        // 支持所有执行目标
        return true
    }


    /**
     * Returns UI Editor for this Run Configuration
     */
    override fun getConfigurationEditor() = ConfigEditor(project, this)


    /**
     * Suggests Name for Run configurations created from Context (by right-clicking and run)
     */
    override fun suggestedName(): String? {
        if (solutionFilePath.isEmpty())
            return null

        if (solutionFilePath in solutionFiles) {
            val id = solutionFiles[solutionFilePath]!!.linkedProblemId
            if (id != null) {
                val name = project.autoCp().problems[id.first]?.get(id.second)?.name
                if (name != null)
                    return name
            }
        }

        return Path(solutionFilePath).nameWithoutExtension
    }

    override fun writeExternal(element: Element) {
        XmlSerializer.serializeInto(this, element)
        super.writeExternal(element)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        XmlSerializer.deserializeInto(this, element)
    }

}