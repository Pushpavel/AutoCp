package com.github.pushpavel.autocp.config

import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.tester.AutoCpTestingProcessHandler
import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import com.intellij.openapi.project.Project
import com.intellij.openapi.diagnostic.Logger


open class AutoCpRunState(val project: Project, protected val config: AutoCpConfig) : RunProfileState {

    companion object {
        private val LOG = Logger.getInstance(AutoCpRunState::class.java)
    }

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        LOG.warn("AutoCp Debug: AutoCpRunState.execute() called")
        LOG.warn("AutoCp Debug: Config name = ${config.name}")
        LOG.warn("AutoCp Debug: Solution file path = ${config.solutionFilePath}")
        // prepare testing process
        val processHandler = AutoCpTestingProcessHandler(project, config, false)
        LOG.warn("AutoCp Debug: AutoCpTestingProcessHandler created successfully")

        // prepare console
        val properties = SMTRunnerConsoleProperties(config, R.strings.runConfigName, executor)
        val console =
            SMTestRunnerConnectionUtil.createAndAttachConsole(R.strings.runConfigName, processHandler, properties)
        LOG.warn("AutoCp Debug: Console created successfully")
        return DefaultExecutionResult(console, processHandler)
    }
}