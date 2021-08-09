package tester

import common.errors.Err.TesterErr.SolutionFileErr
import common.errors.mapToErr
import config.AutoCpConfig
import database.autoCp
import database.models.SolutionFile
import tester.base.SolutionProcessFactory
import tester.base.TestingProcessHandler
import tester.tree.TestNode
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.pathString

/**
 * [TestingProcessHandler] implementation that creates a [TestcaseTreeTestingProcess] for execution
 */
class AutoCpTestingProcessHandler(private val config: AutoCpConfig) : TestingProcessHandler() {

    private val reporter = TreeTestingProcessReporter(this)
    private val db = config.project.autoCp()

    override suspend fun createTestingProcess() = runCatching {
        // get and validate SolutionFile from config
        val solutionFile = getValidSolutionFile()

        // Build Executable from Solution File
        val processFactory = SolutionProcessFactory.buildFromConfig(config)

        // Build Test tree
        val rootNode = solutionFileToTestNode(solutionFile, processFactory)

        // create a TestingProcess from the Problem and Test Tree
        return@runCatching TestcaseTreeTestingProcess(rootNode, reporter)
    }.onFailure {
        reporter.testingProcessStartErrored(it.mapToErr())
    }.getOrNull()

    private fun getValidSolutionFile(): SolutionFile {
        // fixme: this validation logic is duplicated with config package
        // fixme: Path may throw if solutionFilePath is in invalid format
        val solutionPath = Path(config.solutionFilePath)

        if (!solutionPath.exists())
            throw SolutionFileErr("Solution File \"${solutionPath.pathString}\" does not exists")


        return db.solutionFiles[config.solutionFilePath]
            ?: throw SolutionFileErr("Solution File \"${solutionPath.pathString}\" is not associated with any problem.")
    }


    private fun solutionFileToTestNode(solutionFile: SolutionFile, processFactory: SolutionProcessFactory): TestNode {
        val leafNodes = solutionFile.testcases.map {
            TestNode.Leaf(it.name, it.input, it.output, processFactory)
        }

        // TODO: remove this dependence on Problem
        val problem = db.problems[solutionFile.linkedProblemId?.first]?.get(solutionFile.linkedProblemId?.second)
            ?: throw SolutionFileErr("Solution File \"${solutionFile.pathString}\" is not associated with any Problem parsed by AutoCp")

        return TestNode.Group(
            Path(solutionFile.pathString).nameWithoutExtension,
            problem.timeLimit,
            leafNodes,
            processFactory
        )
    }
}