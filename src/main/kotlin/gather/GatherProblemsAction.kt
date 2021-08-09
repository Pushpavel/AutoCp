package gather

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.DumbAware
import common.helpers.causes
import common.helpers.mainScope
import database.autoCp
import gather.server.createServer
import gather.server.getResponsesAsync
import gather.ui.GatheringReporterDialog
import gather.ui.solutionsDialog.SolutionsDialog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.net.ServerSocket
import java.net.SocketTimeoutException

/**
 * [AnAction] that opens a dialog and starts a server waiting for
 * competitive companion browser extension to parse the problems
 * and finally opens another dialog for generating solution files
 */
class GatherProblemsAction : AnAction(), DumbAware {
    companion object {
        private const val CPH_PORT = 27121
        private const val TIMEOUT = 20000L // 20 seconds
    }

    private var server: ServerSocket? = null
    private var scope = mainScope()

    @ExperimentalCoroutinesApi
    override fun actionPerformed(event: AnActionEvent) {
        if (isServerActive())
            return

       scope.launch {
            runCatching {

                val project = event.project!!
                server = createServer(CPH_PORT)

                val problems = GatheringReporterDialog(project, this).use { dialog ->
                    // showing modal dialog async
                    invokeLater {
                        dialog.show()
                        server?.close() // close server as dialog closes
                    }

                    server?.use {
                        val responsesChannel = getResponsesAsync(it, TIMEOUT)
                        gatherProblems(responsesChannel, dialog.eventsChannel)
                    }
                } ?: return@launch

                project.autoCp().updateProblems(problems)

                val dialog = SolutionsDialog(problems[0].groupName, problems)

                invokeLater {
                    val selectedLang = dialog.showAndGetLang() ?: return@invokeLater

                    generateSolutionFiles(project, problems, selectedLang)

                    Notifications.Bus.notify(
                        Notification(
                            "autocp-notifications-group",
                            "Generated solution files",
                            problems[0].groupName,
                            NotificationType.INFORMATION
                        )
                    )
                }
            }.recoverCatching {
                if (it.causes().any { ex -> ex is SocketTimeoutException })
                    Notifications.Bus.notify(
                        Notification(
                            "autocp-notifications-group",
                            "Gathering problems failed",
                            "Competitive Companion browser extension taking longer to respond or is not responding",
                            NotificationType.ERROR
                        )
                    )
                else
                    it.printStackTrace()
            }

        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null && !isServerActive()
    }

    private fun isServerActive(): Boolean {
        return server != null && !server!!.isClosed
    }


}