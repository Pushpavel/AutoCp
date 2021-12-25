package com.github.pushpavel.autocp.core.runner

import com.github.pushpavel.autocp.common.res.R
import com.intellij.execution.Executor
import com.intellij.openapi.util.IconLoader

class AutoCpExecutor : Executor() {
    override fun getToolWindowId() = R.keys.autoCpToolWindowId
    override fun getToolWindowIcon() = R.icons.logo16

    override fun getIcon() = R.icons.runAutoCp
    override fun getDisabledIcon() = IconLoader.getDisabledIcon(R.icons.runAutoCp)

    override fun getDescription() = "Run Testcases on the selected file with AutoCp"
    override fun getActionName() = "Run with AutoCp"

    override fun getId() = R.keys.autoCpExecutorId
    override fun getStartActionText() = "Run Testcases of"

    override fun getContextActionId() = "RunAutoCp"
    override fun getHelpId() = null

}