package tester

import com.github.pushpavel.autocp.database.Problem
import com.intellij.openapi.components.service
import common.errors.Err
import common.errors.Err.TesterErr.SolutionFileErr
import config.AutoCpConfig
import database.AcpDatabase
import tester.base.SolutionProcessFactory
import tester.base.TestingProcessHandler
import tester.tree.TestNode
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString

class AutoCpTestingProcessHandler(val config: AutoCpConfig) : TestingProcessHandler() {

    private val reporter = TreeTestingProcessReporter(this)

    override suspend fun createTestingProcess() = runCatching {
        // get and validate Problem from config
        val problem = getValidProblem()

        // Build Executable from Solution File
        val processFactory = SolutionProcessFactory.buildFromConfig(config)

        // Build Test tree
        val rootNode = problemToTestNode(problem, processFactory)

        // create a TestingProcess from the Problem and Test Tree
        return@runCatching TestcaseTreeTestingProcess(rootNode, reporter)
    }.onFailure {
        val error = when (it) {
            is Err -> it
            else -> Err.InternalErr(it.stackTraceToString())
        }
        reporter.testingProcessStartErrored(error)
    }.getOrNull()

    private fun getValidProblem(): Problem {
        val solutionPath = Path(config.solutionFilePath)

        if (!solutionPath.exists())
            throw SolutionFileErr("Solution File \"${solutionPath.pathString}\" does not exists")

        val database = config.project.service<AcpDatabase>()

        return database.getProblem(config.solutionFilePath)
            .getOrThrow()
            ?: throw SolutionFileErr("Solution File \"${solutionPath.pathString}\" is not associated with any problem.")
    }


    private fun problemToTestNode(problem: Problem, processFactory: SolutionProcessFactory): TestNode {
        val leafNodes = problem.testcases.map {
            TestNode.Leaf(it.name, it.name, it.input, it.output, processFactory)
        }
        return TestNode.Group(problem.name, problem.name, leafNodes, processFactory)
    }
}