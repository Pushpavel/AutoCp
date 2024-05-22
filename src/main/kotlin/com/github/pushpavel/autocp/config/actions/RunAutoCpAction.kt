package com.github.pushpavel.autocp.config.actions

import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.config.AutoCpConfigProducer
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.impl.ExecutionManagerImpl
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.DumbAware


class RunAutoCpAction : AnAction("Run with AutoCp", "Run the currently focused file with AutoCp", R.icons.logo13),
    DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        try {
            val configProducer = RunConfigurationProducer.getInstance(AutoCpConfigProducer::class.java)
            val configurationContext = ConfigurationContext.getFromContext(
                e.dataContext,
                "context aware place" /*fixme: I don't know what I am doing*/
            )
            val configurationFromContext = configProducer.createConfigurationFromContext(configurationContext)

            val runnerAndConfigurationSettings =
                configurationFromContext?.configurationSettings ?: throw IllegalStateException()
            val executor = DefaultRunExecutor.getRunExecutorInstance()
            val runContentDescriptor = ExecutionManagerImpl.getInstance(e.project!!)
                .getDescriptors { it.uniqueID == runnerAndConfigurationSettings.uniqueID }.firstOrNull()
            if (runContentDescriptor == null || runnerAndConfigurationSettings.configuration.isAllowRunningInParallel)
                ExecutionUtil.runConfiguration(runnerAndConfigurationSettings, executor)
            else
                ExecutionUtil.restart(runContentDescriptor)
        } catch (e: IllegalStateException) {
            R.notify.noConfigInContext()
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
