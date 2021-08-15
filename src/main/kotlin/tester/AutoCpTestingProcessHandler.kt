package tester

import common.res.R
import config.AutoCpConfig
import config.validators.NoBuildConfigErr
import config.validators.SolutionFilePathErr
import config.validators.getValidBuildConfig
import config.validators.getValidSolutionFile
import database.models.SolutionFile
import tester.base.SolutionProcessFactory
import tester.base.TestingProcessHandler
import tester.tree.TestNode
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

/**
 * [TestingProcessHandler] implementation that creates a [TestcaseTreeTestingProcess] for execution
 */
class AutoCpTestingProcessHandler(private val config: AutoCpConfig) : TestingProcessHandler() {

    private val reporter = TreeTestingProcessReporter(this)

    override suspend fun createTestingProcess(): TestcaseTreeTestingProcess? {
        try {
            // get and validate SolutionFile from config
            val solutionFile = getValidSolutionFile(config.project, config.name, config.solutionFilePath)

            // validate buildConfig
            val buildConfig = getValidBuildConfig(solutionFile, config.buildConfigId)

            // TODO: report compile logs and errors
            // Build Executable from Solution File
            val processFactory = SolutionProcessFactory.from(solutionFile, buildConfig)

            // Build Test tree
            val rootNode = solutionFileToTestNode(solutionFile, processFactory)

            // create a TestingProcess from the Problem and Test Tree
            return TestcaseTreeTestingProcess(rootNode, reporter)
        } catch (err: Exception) {
            reportTestingStartErr(err)
            return null
        }
    }


    private fun solutionFileToTestNode(solutionFile: SolutionFile, processFactory: SolutionProcessFactory): TestNode {
        val leafNodes = solutionFile.testcases.map {
            TestNode.Leaf(it.name, it.input, it.output, processFactory)
        }

        return TestNode.Group(
            Path(solutionFile.pathString).nameWithoutExtension,
            solutionFile.timeLimit,
            leafNodes,
            processFactory
        )
    }

    private fun reportTestingStartErr(err: Exception) {
        val message = when (err) {
            is SolutionFilePathErr -> R.strings.solutionFilePathErrMsg(err)
            is NoBuildConfigErr -> R.strings.noBuildConfigFoundMsg(err)
            else -> throw err
        }

        reporter.testingProcessError(message)
    }
}