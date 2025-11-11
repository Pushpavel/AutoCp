package com.github.pushpavel.autocp.config

import com.intellij.execution.ExecutionException
import com.intellij.execution.ExecutionResult
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.GenericProgramRunner
import com.intellij.execution.ui.RunContentDescriptor

class AutoCpProgramRunner : GenericProgramRunner<RunnerSettings>() {
    
    override fun getRunnerId(): String {
        return "AutoCpProgramRunner"
    }
    
    override fun canRun(executorId: String, profile: RunProfile): Boolean {
        // Support AutoCpConfig for all executors
        return profile is AutoCpConfig
    }
    
    @Throws(ExecutionException::class)
    override fun doExecute(state: RunProfileState, environment: ExecutionEnvironment): RunContentDescriptor? {
        // Execute the state and get the result
        val executionResult: ExecutionResult = state.execute(environment.executor, this)
            ?: throw ExecutionException("AutoCp: Failed to execute run state - state.execute() returned null")
        
        // Create and return the RunContentDescriptor
        val consoleComponent = executionResult.executionConsole?.component
            ?: throw ExecutionException("AutoCp: Console component is null")
        
        return RunContentDescriptor(
            executionResult.executionConsole,
            executionResult.processHandler,
            consoleComponent,
            environment.runProfile.name
        )
    }
}
