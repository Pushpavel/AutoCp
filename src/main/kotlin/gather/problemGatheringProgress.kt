package gather

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import gather.models.GatheringResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter


class ProgressReporter(
    scope: CoroutineScope,
    project: Project,
    private val gathers: MutableSharedFlow<GatheringResult>,
    private val cancelCallback: () -> Unit
) : Task.Backgroundable(project, "Gathering problems...") {

    var firstGather: GatheringResult.Gathered? = null

    init {
        scope.launch {

            // filters to only the first gather of the batch
            // leaving rest to be handled inside progress indicator
            gathers.filter {
                when (it) {
                    is GatheringResult.Gathered -> it.problems.size == 1
                    else -> false
                }
            }.collect {
                firstGather = it as GatheringResult.Gathered
                ProgressManager.getInstance().run(this@ProgressReporter)
            }
        }
    }

    override fun run(indicator: ProgressIndicator) {
        var job: Job? = null


        fun showProgress(indicator: ProgressIndicator, event: GatheringResult.Gathered) {
            indicator.fraction = event.problems.size.toDouble() / event.batch.size
            indicator.text = event.problems.last().name
            indicator.text2 = event.problems.last().groupName

            if (event.problems.size == event.batch.size)
                job?.cancel()
        }

        firstGather?.let { showProgress(indicator, it) }


        runBlocking {
            job = launch {
                gathers.collect {
                    when (it) {
                        is GatheringResult.Gathered -> showProgress(indicator, it)
                        else -> job?.cancel()
                    }
                }
            }

            while (!indicator.isCanceled && job?.isCancelled == false)
                delay(100)

            if (indicator.isCanceled)
                cancelCallback()

            job?.cancel()
        }
    }

}


