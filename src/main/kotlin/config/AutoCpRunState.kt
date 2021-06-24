package config

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import common.Constants
import tester.AutoCpTestingProcessHandler


class AutoCpRunState(private val config: AutoCpConfig) : RunProfileState {

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        // prepare testing process
        val processHandler = AutoCpTestingProcessHandler(config)

        // prepare console
        val properties = SMTRunnerConsoleProperties(config, Constants.FrameworkName, executor)
        val console =
            SMTestRunnerConnectionUtil.createAndAttachConsole(Constants.FrameworkName, processHandler, properties)

        return DefaultExecutionResult(console, processHandler)
    }
}