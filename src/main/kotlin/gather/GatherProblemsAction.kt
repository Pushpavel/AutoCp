package gather

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import database.AcpDatabase
import gather.server.createServer
import gather.server.getResponsesAsync
import gather.ui.GatheringReporterDialog
import gather.ui.GenerateSolutionsDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import java.net.ServerSocket


class GatherProblemsAction : AnAction(), DumbAware {
    companion object {
        private const val CPH_PORT = 27121
    }

    private var server: ServerSocket? = null

    @ExperimentalCoroutinesApi
    override fun actionPerformed(event: AnActionEvent) {
        if (server?.isClosed == true)
            return

        GlobalScope.launch(Dispatchers.Swing) {
            val project = event.project!!
            server = createServer(CPH_PORT)

            val problems = GatheringReporterDialog(project, this).use { dialog ->
                // showing modal dialog async
                invokeLater {
                    dialog.show()
                    server?.close() // close server as dialog closed
                }

                server?.use {
                    val responsesChannel = getResponsesAsync(it)
                    gatherProblems(responsesChannel, dialog.eventsChannel)
                }
            } ?: return@launch

            val service = project.service<AcpDatabase>()

            service.insertProblems(problems).getOrThrow()

            val dialog = GenerateSolutionsDialog(project, problems)
            val result = dialog.getResult() ?: return@launch

            generateSolutionFiles(project, result)

            Notifications.Bus.notify(
                Notification(
                    "AutoCp Notification Group",
                    "Generated solution files",
                    result.problems[0].groupName + " files generated",
                    NotificationType.INFORMATION
                )
            )

        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null && server?.isClosed != true
    }
}