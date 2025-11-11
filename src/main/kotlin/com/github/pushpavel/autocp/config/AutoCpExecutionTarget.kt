package com.github.pushpavel.autocp.config

import com.intellij.execution.ExecutionTarget
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project

/**
 * Custom ExecutionTarget for AutoCp that declares it can run independently
 * of any build system targets (like CMake Debug/Release).
 * 
 * This is used to bypass ExecutionTargetManager.doCanRun() checks that
 * prevent AutoCp from running when a CMake build target is selected.
 */
class AutoCpExecutionTarget : ExecutionTarget() {
    
    override fun getId(): String = "AutoCp-Default-Target"
    
    override fun getDisplayName(): String = "AutoCp (Default)"
    
    override fun getIcon() = null
    
    override fun canRun(configuration: RunConfiguration): Boolean {
        // AutoCp execution target can only run AutoCp configurations
        return configuration is AutoCpConfig
    }
    
    override fun isExternallyManaged(): Boolean = false
    
    companion object {
        private val instance = AutoCpExecutionTarget()
        
        fun getInstance(): AutoCpExecutionTarget = instance
    }
}
