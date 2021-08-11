package gather

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import common.res.R
import gather.models.ProblemGatheredEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProblemGatheringProgressReporter(
    project: Project?,
    val cancelCallback: () -> Unit
) : Task.Backgroundable(project, R.strings.problemGatheringTitle) {

    var progress: MutableSharedFlow<ProblemGatheredEvent>? = null
    var completed = true

    override fun run(indicator: ProgressIndicator) {
        indicator.isIndeterminate = false
        completed = false

        runBlocking {

            val job = launch {
                progress?.collectLatest {
                    indicator.fraction = (it.gathered / it.total).toDouble()
                    indicator.text = it.problem.name
                    indicator.text2 = it.problem.groupName
                }
            }

            @Suppress("ControlFlowWithEmptyBody")
            while (!indicator.isCanceled && !completed);

            if (indicator.isCanceled)
                cancelCallback()

            job.cancel()

        }
    }

    fun start() {
        progress = MutableSharedFlow(1)
        ProgressManager.getInstance().run(this)
    }

    fun stop() {
        completed = true
    }

}

