package gather

import com.intellij.execution.runners.ExecutionUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import common.res.R

class ProblemGatheringAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val service = e.project!!.service<ProblemGatheringService>()

        if (service.isRunning())
            service.stopService()
        else
            service.startServiceAsync()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.project != null

        if (!e.presentation.isEnabled) return

        val service = e.project!!.service<ProblemGatheringService>()
        if (service.isRunning()) {
            e.presentation.icon = ExecutionUtil.getLiveIndicator(AllIcons.Webreferences.Server)
            e.presentation.text = R.strings.stopGatheringText
            e.presentation.description = R.strings.stopGatheringDesc
        } else {
            e.presentation.icon = (AllIcons.Webreferences.Server)
            e.presentation.text = R.strings.startGatheringText
            e.presentation.description = R.strings.startGatheringDesc
        }
    }
}