package com.github.pushpavel.autocp.config.stress

import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.config.AutoCpRunState
import com.github.pushpavel.autocp.tester.AutoCpTestingProcessHandler
import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key

class AutoCpStressRunState(project: Project, config: AutoCpStressConfig) : AutoCpRunState(project, config) {

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {

        val processHandler = AutoCpTestingProcessHandler(project, config, true)

        val properties = SMTRunnerConsoleProperties(config, R.strings.runStressConfigName, executor)
        val console = SMTestRunnerConnectionUtil.createAndAttachConsole(R.strings.runStressConfigName, processHandler, properties)

        return DefaultExecutionResult(console, processHandler)

    }

}
