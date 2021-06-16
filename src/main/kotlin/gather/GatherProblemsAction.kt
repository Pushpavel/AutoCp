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
import gather.ui.GenerateSolutionsDialogModel
import gather.ui.createGenerateSolutionsDialog
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
            runCatching {

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
                val model = GenerateSolutionsDialogModel(project, problems)
                val dialog = createGenerateSolutionsDialog(model)

                invokeLater {
                    if (!dialog.showAndGet())
                        return@invokeLater

                    val selectedLang = model.langModel.selected ?: throw Exception("No Solution Language selected")

                    generateSolutionFiles(project, problems, selectedLang)

                    Notifications.Bus.notify(
                        Notification(
                            "AutoCp Notification Group",
                            "Generated solution files",
                            problems[0].groupName,
                            NotificationType.INFORMATION
                        )
                    )
                }
            }.onFailure {
                it.printStackTrace()
            }

        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null && server?.isClosed != true
    }
}