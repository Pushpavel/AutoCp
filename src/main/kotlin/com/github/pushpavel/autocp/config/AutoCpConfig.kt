package com.github.pushpavel.autocp.config

import com.github.pushpavel.autocp.database.SolutionFiles
import com.github.pushpavel.autocp.database.autoCp
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.target.TargetEnvironmentAwareRunProfile
import com.intellij.execution.target.TargetEnvironmentConfiguration
import com.intellij.execution.target.LanguageRuntimeType
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializer
import org.jdom.Element
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension
import com.intellij.openapi.diagnostic.Logger
import com.intellij.execution.ExecutionTarget

/**
 * Implementation Class for a Custom Run Configuration that can also be created from context (by right-clicking and run)
 * 
 * Implements TargetEnvironmentAwareRunProfile to explicitly declare that AutoCp does not depend on
 * any ExecutionTarget and should be able to run independently of CMake or other target configurations.
 */
open class AutoCpConfig(project: Project, factory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase<RunProfileState>(project, factory, name), 
    TargetEnvironmentAwareRunProfile {
    companion object {
        private val LOG = Logger.getInstance(AutoCpConfig::class.java)
    }

    init {
        LOG.warn("AutoCp Debug: AutoCpConfig instance created, name='$name', factory='${factory.id}'")
        // Allow multiple AutoCp test runs to execute in parallel
        // This enables users to press Alt+X multiple times to run the same file concurrently
        isAllowRunningInParallel = true
        LOG.warn("AutoCp Debug: isAllowRunningInParallel set to true")
    }

    var solutionFilePath: String = ""

    private val solutionFiles = SolutionFiles.getInstance(project)

    /**
     * Returns [RunProfileState] that defines the execution of this Run Configuration
     */
    override fun getState(executor: Executor, environment: ExecutionEnvironment) : RunProfileState? {
        LOG.warn("AutoCp Debug: ========== getState() CALLED ==========")
        LOG.warn("AutoCp Debug: Executor = ${executor.id}")
        LOG.warn("AutoCp Debug: ExecutionTarget = ${environment.executionTarget?.displayName}")
        LOG.warn("AutoCp Debug: ExecutionTarget ID = ${environment.executionTarget?.id}")
        LOG.warn("AutoCp Debug: Solution file path = $solutionFilePath")
        LOG.warn("AutoCp Debug: Config name = $name")
        LOG.warn("AutoCp Debug: Runner = ${environment.runner?.runnerId}")
        
        // Check canRunOn for current execution target
        if (environment.executionTarget != null) {
            val canRun = canRunOn(environment.executionTarget!!)
            LOG.warn("AutoCp Debug: canRunOn(environment.executionTarget) = $canRun")
        }
        
        LOG.warn("AutoCp Debug: Creating AutoCpRunState...")
        val state = AutoCpRunState(project, this)
        LOG.warn("AutoCp Debug: ==========================================")
        return state
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
        LOG.warn("AutoCp Debug: ========== canRunOn() CALLED ==========")
        LOG.warn("AutoCp Debug: canRunOn(target) with displayName: '${target.displayName}'")
        LOG.warn("AutoCp Debug: canRunOn(target) with id: '${target.id}'")
        LOG.warn("AutoCp Debug: canRunOn(target) class: ${target.javaClass.name}")
        LOG.warn("AutoCp Debug: Current config name: '$name'")
        LOG.warn("AutoCp Debug: Current solutionFilePath: '$solutionFilePath'")
        
        // AutoCp is an independent test run configuration that does not depend on any specific ExecutionTarget
        // It should be able to run on any ExecutionTarget (including CMake build configurations)
        // During actual execution, AutoCp uses its own test execution logic and ignores the ExecutionTarget
        val result = true
        LOG.warn("AutoCp Debug: canRunOn() returning: $result (always true - AutoCp is ExecutionTarget-independent)")
        LOG.warn("AutoCp Debug: ========================================")
        return result
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

    // TargetEnvironmentAwareRunProfile implementation
    
    /**
     * Returns false to indicate that AutoCp does not need target environment preparation.
     * AutoCp runs its own testing process and does not depend on any ExecutionTarget.
     */
    override fun needPrepareTarget(): Boolean {
        LOG.warn("AutoCp Debug: needPrepareTarget() called, returning false")
        return false
    }

    /**
     * Returns true to indicate that AutoCp can run on the local machine.
     * This allows AutoCp to bypass ExecutionTarget checks.
     */
    override fun canRunOn(target: TargetEnvironmentConfiguration): Boolean {
        LOG.warn("AutoCp Debug: canRunOn(TargetEnvironmentConfiguration) called")
        return true
    }

    /**
     * Returns null as AutoCp does not use a default target name.
     */
    override fun getDefaultTargetName(): String? {
        LOG.warn("AutoCp Debug: getDefaultTargetName() called, returning null")
        return null
    }

    /**
     * Sets the default target name. AutoCp does not use this, so this is a no-op.
     */
    override fun setDefaultTargetName(targetName: String?) {
        LOG.warn("AutoCp Debug: setDefaultTargetName() called with: $targetName (ignored)")
        // No-op: AutoCp does not use target names
    }

    /**
     * Returns null as AutoCp does not require a language runtime type.
     */
    override fun getDefaultLanguageRuntimeType(): LanguageRuntimeType<*>? {
        LOG.warn("AutoCp Debug: getDefaultLanguageRuntimeType() called, returning null")
        return null
    }

}