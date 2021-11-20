package com.github.pushpavel.autocp.config

import com.github.pushpavel.autocp.common.helpers.doDisposal
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.core.runner.judge.JudgingProcess
import com.github.pushpavel.autocp.core.runner.judge.JudgingProcessListener
import com.github.pushpavel.autocp.core.runner.runtool.ProcessLikeHandler
import com.github.pushpavel.autocp.core.runner.runtool.RunToolNotificationBridge
import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project


class AutoCpRunState(val project: Project, private val config: AutoCpConfig) : RunProfileState {

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        // prepare testing process
        val processHandler = ProcessLikeHandler {
            val parentDisposable = Disposable {}
            val listener = RunToolNotificationBridge(this)
            project.messageBus.connect(parentDisposable).subscribe(JudgingProcessListener.TOPIC, listener)
            project.service<JudgingProcess>().execute(config.solutionFilePath)
            parentDisposable.doDisposal()
        }

        // prepare console
        val properties = SMTRunnerConsoleProperties(config, R.strings.runConfigName, executor)
        val console =
            SMTestRunnerConnectionUtil.createAndAttachConsole(R.strings.runConfigName, processHandler, properties)

        return DefaultExecutionResult(console, processHandler)
    }
}