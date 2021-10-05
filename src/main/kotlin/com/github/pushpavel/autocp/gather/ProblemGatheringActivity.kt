package com.github.pushpavel.autocp.gather

import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

/**
 * Starts a server and generates files arriving from competitive companion
 */
class ProblemGatheringActivity : StartupActivity, DumbAware {

    override fun runActivity(project: Project) = project.service<ProblemGatheringService>().startServiceAsync()
}