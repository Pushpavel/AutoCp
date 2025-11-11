package com.github.pushpavel.autocp.config

import com.intellij.execution.ExecutionTarget
import com.intellij.execution.ExecutionTargetProvider
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project

/**
 * Provides a custom ExecutionTarget for AutoCp configurations.
 * This ensures AutoCp always uses its own execution target instead of
 * the currently selected CMake build target.
 */
class AutoCpExecutionTargetProvider : ExecutionTargetProvider() {
    
    override fun getTargets(project: Project, configuration: RunConfiguration): List<ExecutionTarget> {
        // Only provide AutoCp target for AutoCp configurations
        return if (configuration is AutoCpConfig) {
            listOf(AutoCpExecutionTarget.getInstance())
        } else {
            emptyList()
        }
    }
}
