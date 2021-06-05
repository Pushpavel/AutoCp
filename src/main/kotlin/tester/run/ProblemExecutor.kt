package tester.run

import com.intellij.execution.process.ProcessHandler
import com.intellij.openapi.components.service
import com.intellij.openapi.util.Disposer
import files.ProblemSpecManager
import plugin.config.AutoCpConfig
import tester.spec.TestGroupSpec
import tester.TestGroupExecutor
import tester.process.ProcessLike

class ProblemExecutor(
    private val config: AutoCpConfig,
    private val processHandler: ProcessHandler
) : ProcessLike {

    private fun getProblemSpec() = config.project.service<ProblemSpecManager>().findSpec(config.solutionFilePath)

    override fun start() {
        val reporter = TestReporter(processHandler)
        val problemSpec = getProblemSpec()

        if (problemSpec == null) {
            reporter.logError("Solution File ${config.solutionFilePath}: Does not correspond to a valid .autocp file in {ProjectRoot}/.autocp folder")
            processHandler.destroyProcess()
            return
        }

        val group = TestGroupSpec.fromProblem(problemSpec, config.executablePath)
        val groupExecutor = TestGroupExecutor(group, reporter)

        Disposer.register(this, groupExecutor)

        groupExecutor.start() // blocking
        processHandler.destroyProcess()
    }

    override fun dispose() = Unit //ignore
}