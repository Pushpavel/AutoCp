package actions

import com.google.gson.Gson
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import common.AutoCpProblem
import common.runUI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jetbrains.concurrency.runAsync
import services.AutoCpFilesService
import services.GatherProblemsService
import ui.GatherProblemsDialogUI


class GatherProblemsAction : AnAction(), DumbAware {

    private val gatherService = service<GatherProblemsService>()
    private val ui = GatherProblemsDialogUI()


    override fun actionPerformed(event: AnActionEvent) {
        val problems = gatherProblems(event.project)
        if (problems != null)
            processProblems(event.project, problems)
    }

    private fun gatherProblems(project: Project?): List<AutoCpProblem>? {
        val dialog = ui.getGatheringDialog(project)
        var problems: List<AutoCpProblem>? = null

        runAsync {
            problems = gatherService.gatherProblems() // blocking code
            runUI {
                ui.closeDialog(dialog)
            }
        }

        dialog.show() // blocking code
        return problems
    }

    private fun processProblems(project: Project?, problems: List<AutoCpProblem>) {
        val dialog = ui.getGenerateFilesDialog(project, problems)
        if (dialog.show() != DialogWrapper.OK_EXIT_CODE)
            return

        val service = project?.service<AutoCpFilesService>()

        problems.forEach {
            service?.createAutoCpFile("cpp", it)
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
        e.presentation.isEnabledAndVisible = project != null
    }
}