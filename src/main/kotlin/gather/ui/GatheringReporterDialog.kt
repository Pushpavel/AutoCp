package gather.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogWrapper.OK_EXIT_CODE
import com.intellij.ui.components.JBLabel
import com.intellij.ui.layout.panel
import gather.models.ProblemGatheredEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class GatheringReporterDialog(project: Project, parentScope: CoroutineScope) : AutoCloseable {

    private val headerLabel = JBLabel("Open browser and parse problem or contest using competitive companion extension")
    private val statusLabel = JBLabel("Listening for competitive companion...")
    private val dialog = DialogBuilder(project)
    val eventsChannel = Channel<ProblemGatheredEvent>()
    var job: Job

    init {
        dialog.apply {
            title("Gathering Problems")
            resizable(false)
            removeAllActions()
            addCancelAction().setText("Stop")
            centerPanel(
                panel {
                    row { headerLabel() }
                    row { statusLabel() }
                }
            )
        }

        job = parentScope.launch {
            for (event in eventsChannel) {
                headerLabel.text = "Gathering problems from \"${event.problem.groupName}\""
                statusLabel.text = "(${event.gathered}/${event.total}) - ${event.problem.name}"
            }
        }
    }

    fun show() = dialog.show()

    override fun close() {
        dialog.dialogWrapper.close(OK_EXIT_CODE)
        job.cancel()
    }

}