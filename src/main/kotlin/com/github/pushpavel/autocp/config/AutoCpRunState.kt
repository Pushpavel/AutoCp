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


class AutoCpRunState(val project: Project, private val config: AutoCpConfig) : RunProfileState {

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        // prepare testing process
        val processHandler = AutoCpTestingProcessHandler(project, config)

        // prepare console
        val properties = SMTRunnerConsoleProperties(config, R.strings.runConfigName, executor)
        val console =
            SMTestRunnerConnectionUtil.createAndAttachConsole(R.strings.runConfigName, processHandler, properties)

        return DefaultExecutionResult(console, processHandler)
    }
}