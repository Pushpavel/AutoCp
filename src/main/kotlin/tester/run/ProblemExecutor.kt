package tester.run

import com.intellij.execution.process.ProcessHandler
import com.intellij.openapi.components.service
import com.intellij.openapi.util.Disposer
import database.AcpDatabase
import dev.pushpavel.autocp.database.Problem
import plugin.config.AutoCpConfig
import tester.spec.TestGroupSpec
import tester.TestGroupExecutor
import tester.process.ProcessLike
import kotlin.IllegalStateException
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString

class ProblemExecutor(
    private val config: AutoCpConfig,
    private val processHandler: ProcessHandler
) : ProcessLike {

    private fun getValidProblem(): Problem {
        val solutionPath = Path(config.solutionFilePath)

        if (!solutionPath.exists())
            throw IllegalStateException("Solution File \"${solutionPath.pathString}\" does not exists")

        val database = config.project.service<AcpDatabase>()

        return database.getProblem(config.solutionFilePath)
            .getOrThrow()
            ?: throw IllegalArgumentException("Solution File \"${solutionPath.pathString}\" is not associated with any problem.")
    }

    override fun start() {
        val reporter = TestReporter(processHandler)
        val problem = getValidProblem()

        // building the executable
        val executableBuilder = ExecutableBuilder(problem, config)

        reporter.logOutput("build command: ${executableBuilder.commandLine.commandLineString}")
        val result = executableBuilder.execute()
        reporter.logOutput(result.output)
        reporter.logError(result.error)
        if (result.error.isNotEmpty()) {
            processHandler.destroyProcess()
            return
        }

        // running the executable
        val group = TestGroupSpec.fromProblem(problem, executableBuilder.outputPath)
        val groupExecutor = TestGroupExecutor(group, reporter)

        Disposer.register(this, groupExecutor)

        groupExecutor.start() // blocking
        processHandler.destroyProcess()
    }

    override fun dispose() = Unit //ignore
}