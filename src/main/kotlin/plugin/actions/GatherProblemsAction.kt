package plugin.actions

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import common.runUI
import files.ProblemSpec
import files.ProblemSpecManager
import org.jetbrains.concurrency.runAsync
import plugin.services.GatherProblemsService
import plugin.ui.GatherProblemsDialogUI


class GatherProblemsAction : AnAction(), DumbAware {

    private val service = service<GatherProblemsService>()
    private val ui = GatherProblemsDialogUI()


    override fun actionPerformed(event: AnActionEvent) {
        val problems = gatherProblems(event.project!!)
        if (problems != null)
            processProblems(event.project!!, problems)
    }

    private fun gatherProblems(project: Project?): List<ProblemSpec>? {
        val dialog = ui.getGatheringDialog(project)
        var problems: List<ProblemSpec>? = null

        runAsync {
            problems = service.gatherProblems() // blocking code
            runUI {
                ui.closeDialog(dialog)
            }
        }

        dialog.show() // blocking code
        return problems
    }

    private fun processProblems(project: Project, problems: List<ProblemSpec>) {
        val dialog = ui.getGenerateFilesDialog(project, problems)

        if (dialog.show() != DialogWrapper.OK_EXIT_CODE)
            return

        val specManager = project.service<ProblemSpecManager>()

        problems.forEach {
            specManager.createSpec(it)
        }

        Notifications.Bus.notify(
            Notification(
                "AutoCp Notification Group",
                "Generated solution files",
                problems[0].group + " files generated",
                NotificationType.INFORMATION
            )
        )
    }


    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null && !service.isGathering()
    }
}