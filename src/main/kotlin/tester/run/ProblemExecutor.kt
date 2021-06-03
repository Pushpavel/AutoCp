package tester.run

import com.intellij.execution.process.ProcessHandler
import com.intellij.openapi.components.service
import com.intellij.openapi.util.Disposer
import plugin.config.AutoCpConfig
import plugin.services.AutoCpFilesService
import tester.spec.TestGroupSpec
import tester.TestGroupExecutor
import tester.process.ProcessLike

class ProblemExecutor(
    private val config: AutoCpConfig,
    private val processHandler: ProcessHandler
) : ProcessLike {

    private fun getProblem() = config.project.service<AutoCpFilesService>().getAutoCp(config.problemName)

    override fun start() {
        val reporter = TestReporter(processHandler)
        val problem = getProblem()

        if (problem == null) {
            reporter.logError("Problem ${config.problemName}: Does not correspond to a valid .autocp file in {ProjectRoot}/.autocp folder")
            processHandler.destroyProcess()
            return
        }

        val spec = TestGroupSpec.fromProblem(problem)
        val group = TestGroupExecutor(spec, reporter)

        Disposer.register(this, group)

        group.start() // blocking
        processHandler.destroyProcess()
    }

    override fun dispose() = Unit //ignore
}