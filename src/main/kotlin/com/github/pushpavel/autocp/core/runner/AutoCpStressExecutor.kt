package com.github.pushpavel.autocp.core.runner

import com.github.pushpavel.autocp.common.res.R
import com.intellij.openapi.project.Project

class AutoCpStressExecutor : AutoCpExecutor() {

    override fun getDescription() = "Run Stress tests on the selected file with AutoCp"
    override fun getActionName() = "Stress Test with AutoCp"

    override fun getId() = R.keys.autoCpStressExecutorId
    override fun getStartActionText() = "Run Stress Testing Testcases of"

    override fun getContextActionId() = "RunAutoCpStress"
    override fun getHelpId() = null

    override fun isApplicable(project: Project) = false

}
