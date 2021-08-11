package gather

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import database.models.Problem
import gather.models.BatchJson
import gather.models.ProblemGatheredEvent
import gather.server.ProblemGatheringServer
import gather.server.ProblemGatheringServerListener

/**
 * Starts a server and generates files arriving from competitive companion
 */
class ProblemGatheringActivity : StartupActivity, ProblemGatheringServerListener {

    private lateinit var reporter: ProblemGatheringProgressReporter
    private val server = ProblemGatheringServer(this)

    override fun runActivity(project: Project) {
        reporter = ProblemGatheringProgressReporter(project, this::onCancel)
        server.startServer()
    }

    override fun onBatchStart() = reporter.start()
    override fun onBatchEnd() = reporter.stop()

    override suspend fun onProblem(problems: List<Problem>, batch: BatchJson) {
        reporter.progress?.emit(ProblemGatheredEvent(problems.last(), problems.size, batch.size))
    }


    private fun onCancel() {
        // TODO: ignore current Batch
    }
}