package run

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.NopProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil
import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import common.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AutoCpRunState(private val config: RunConfiguration) : RunProfileState {

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        val processHandler = NopProcessHandler()
        val properties = SMTRunnerConsoleProperties(config, Constants.FrameworkName, executor)
        val console =
            SMTestRunnerConnectionUtil.createAndAttachConsole(Constants.FrameworkName, processHandler, properties)
        processHandler.startNotify()
        GlobalScope.launch {

            processHandler.notifyTextAvailable(
                ServiceMessageBuilder("enteredTheMatrix").toString() + "\n", ProcessOutputTypes.STDOUT
            )
            delay(1000)
            processHandler.notifyTextAvailable(
                ServiceMessageBuilder.testSuiteStarted("Super Config Name").toString() + "\n", ProcessOutputTypes.STDOUT
            )
            delay(1000)
            processHandler.notifyTextAvailable(
                ServiceMessageBuilder.testStarted("Super TestClass Name").toString() + "\n",
                ProcessOutputTypes.STDOUT
            )
            delay(1000)
            processHandler.notifyTextAvailable(
                ServiceMessageBuilder.testFinished("Super TestClass Name").toString() + "\n",
                ProcessOutputTypes.STDOUT
            )
            delay(1000)
            processHandler.notifyTextAvailable(
                ServiceMessageBuilder.testSuiteFinished("Super Config Name").toString() + "\n",
                ProcessOutputTypes.STDOUT
            )
            delay(1000)
            processHandler.destroyProcess()
        }

        return DefaultExecutionResult(console, processHandler)
    }
}