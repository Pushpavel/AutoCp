package com.github.pushpavel.autocp.config.actions

import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.config.AutoCpConfigProducer
import com.github.pushpavel.autocp.config.AutoCpExecutionTarget
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.impl.ExecutionManagerImpl
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.execution.runners.ProgramRunner
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.diagnostic.Logger


class RunAutoCpAction : AnAction("Run with AutoCp", "Run the currently focused file with AutoCp", R.icons.logo13),
    DumbAware {
    
    companion object {
        private val LOG = Logger.getInstance(RunAutoCpAction::class.java)
    }
    
    override fun actionPerformed(e: AnActionEvent) {
        LOG.warn("AutoCp Debug: ========== RunAutoCpAction.actionPerformed CALLED ==========")
        try {
            val project = e.project ?: throw IllegalStateException("Project is null")
            LOG.warn("AutoCp Debug: Project = ${project.name}")
            
            val configProducer = RunConfigurationProducer.getInstance(AutoCpConfigProducer::class.java)
            LOG.warn("AutoCp Debug: ConfigProducer obtained: ${configProducer.javaClass.simpleName}")
            
            val configurationContext = ConfigurationContext.getFromContext(
                e.dataContext,
                "context aware place" /*fixme: I don't know what I am doing*/
            )
            LOG.warn("AutoCp Debug: ConfigurationContext created")
            
            val configurationFromContext = configProducer.createConfigurationFromContext(configurationContext)
            LOG.warn("AutoCp Debug: Configuration from context: ${configurationFromContext != null}")

            val runnerAndConfigurationSettings =
                configurationFromContext?.configurationSettings ?: throw IllegalStateException("Configuration settings is null")
            
            LOG.warn("AutoCp Debug: Configuration name = ${runnerAndConfigurationSettings.name}")
            LOG.warn("AutoCp Debug: Configuration uniqueID = ${runnerAndConfigurationSettings.uniqueID}")
            LOG.warn("AutoCp Debug: Configuration.isAllowRunningInParallel = ${runnerAndConfigurationSettings.configuration.isAllowRunningInParallel}")
            
            val executor = DefaultRunExecutor.getRunExecutorInstance()
            LOG.warn("AutoCp Debug: Executor = ${executor.id}")
            
            val existingDescriptors = ExecutionManagerImpl.getInstance(project)
                .getDescriptors { it.uniqueID == runnerAndConfigurationSettings.uniqueID }
            LOG.warn("AutoCp Debug: Found ${existingDescriptors.size} existing descriptors with same uniqueID")
            
            val runContentDescriptor = existingDescriptors.firstOrNull()
            LOG.warn("AutoCp Debug: runContentDescriptor = ${runContentDescriptor != null}")
            
            val shouldCreateNew = runContentDescriptor == null || runnerAndConfigurationSettings.configuration.isAllowRunningInParallel
            LOG.warn("AutoCp Debug: shouldCreateNew = $shouldCreateNew (descriptor=$runContentDescriptor, allowParallel=${runnerAndConfigurationSettings.configuration.isAllowRunningInParallel})")
            
            if (shouldCreateNew) {
                LOG.warn("AutoCp Debug: Creating NEW execution instance...")
                // CRITICAL FIX: Use custom ExecutionTarget to bypass CMake target checks
                // Create a custom ExecutionEnvironment with AutoCp's own ExecutionTarget
                val runner = ProgramRunner.getRunner(executor.id, runnerAndConfigurationSettings.configuration)
                LOG.warn("AutoCp Debug: ProgramRunner = ${runner?.runnerId ?: "null"}")
                
                if (runner != null) {
                    LOG.warn("AutoCp Debug: Building ExecutionEnvironment with AutoCpExecutionTarget...")
                    val environment = ExecutionEnvironmentBuilder.create(executor, runnerAndConfigurationSettings)
                        .target(AutoCpExecutionTarget.getInstance()) // Force use AutoCp's ExecutionTarget
                        .build()
                    LOG.warn("AutoCp Debug: Calling runner.execute()...")
                    runner.execute(environment)
                    LOG.warn("AutoCp Debug: runner.execute() completed")
                } else {
                    // Fallback to default execution if no suitable runner found
                    LOG.warn("AutoCp Debug: No runner found, using fallback ExecutionUtil.runConfiguration()")
                    ExecutionUtil.runConfiguration(runnerAndConfigurationSettings, executor)
                }
            } else {
                LOG.warn("AutoCp Debug: RESTARTING existing execution instance...")
                ExecutionUtil.restart(runContentDescriptor)
                LOG.warn("AutoCp Debug: ExecutionUtil.restart() completed")
            }
            LOG.warn("AutoCp Debug: ==========================================")
        } catch (e: IllegalStateException) {
            LOG.error("AutoCp Debug: IllegalStateException in actionPerformed", e)
            R.notify.noConfigInContext()
        } catch (e: Exception) {
            LOG.error("AutoCp Debug: Unexpected exception in actionPerformed", e)
            throw e
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.project != null && e.getData(CommonDataKeys.VIRTUAL_FILE) != null
    }

    /* TODO: I have no idea what this means but at least it works */
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }
}
