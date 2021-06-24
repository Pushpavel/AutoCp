package tester.run

import com.intellij.execution.process.ProcessHandler
import com.intellij.openapi.components.service
import com.intellij.openapi.util.Disposer
import database.AcpDatabase
import com.github.pushpavel.autocp.database.Problem
import config.AutoCpConfig
import tester.models.TestGroupSpec
import tester.runner.TestExecutionGroup
import tester.execute.ProcessLike
import kotlin.IllegalStateException
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString

@Deprecated("use ProcessRunner object")
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

        reporter.logOutput("build: ${executableBuilder.commandLine.commandLineString}")
        val result = executableBuilder.execute()
        reporter.logOutput(result.output)
        if (result.error.isNotEmpty()) {
            reporter.logError(result.error)
            processHandler.destroyProcess()
            return
        }

        // verify whether testcases are present
        if (problem.testcases.isEmpty()) {
            reporter.logError("No Testcases found for ${problem.name}.")
            processHandler.destroyProcess()
            return
        }
        // running the executable
        val group = TestGroupSpec.fromProblem(problem, executableBuilder.outputPath)
        val groupExecutor = TestExecutionGroup(group, reporter)

        Disposer.register(this, groupExecutor)

        groupExecutor.start() // blocking
        processHandler.destroyProcess()
    }

    override fun dispose() = Unit //ignore
}