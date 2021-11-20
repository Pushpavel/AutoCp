package com.github.pushpavel.autocp.core.runner.judge

import com.github.pushpavel.autocp.core.execution.BuildSolutionErr
import com.github.pushpavel.autocp.core.execution.ExecutionUtil
import com.github.pushpavel.autocp.core.execution.buildSolutionExecutable
import com.github.pushpavel.autocp.core.persistance.solutions.Solutions
import com.github.pushpavel.autocp.core.persistance.testcases.Testcases
import com.github.pushpavel.autocp.tester.utils.trimByLines
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import kotlinx.coroutines.*

/**
 * Local Judge that executes the solution on the testcases and judges the output.
 */
@Service
class JudgingProcess(val project: Project) {

    private val messageBus = project.messageBus
    private val solutions = project.service<Solutions>()
    private val testcases = project.service<Testcases>()

    /**
     * Executes the solution in [solutionPathString] on the testcases and returns the result.
     */
    suspend fun execute(
        solutionPathString: String,
        buildProgressIndicator: ProgressIndicator? = null
    ): ResultGroupNode? {
        try {
            val buildOutput = buildSolutionExecutable(project, solutionPathString, buildProgressIndicator)

            // gather solution and its testcases
            if (solutionPathString !in solutions)
                throw JudgeErr.NoSolution(solutionPathString)

            val solution = solutions[solutionPathString]!!

            val testcaseList = testcases[solutionPathString]?.toList()

            if (testcaseList == null || testcaseList.isEmpty())
                throw JudgeErr.NoTestcases(solution, solutionPathString)

            messageBus.syncPublisher(JudgingProcessListener.TOPIC).onTestingStarted(solution, testcaseList, buildOutput)

            // construct Judge related data classes
            val testNodes = testcaseList.map { TestNode(buildOutput, it.input, it.output, solution.timeLimit) }
            val testGroupNode = TestGroupNode(solution.displayName, testNodes)

            // execute
            val resultGroupNode = executeTestGroup(testGroupNode)
            messageBus.syncPublisher(JudgingProcessListener.TOPIC).onTestingFinished()

            return resultGroupNode
        } catch (e: Exception) {
            messageBus.syncPublisher(JudgingProcessListener.TOPIC).onTestingFailed(
                when (e) {
                    is BuildSolutionErr -> JudgeErr.BuildErr(e)
                    is JudgeErr -> e
                    else -> JudgeErr.UnknownErr(e)
                }
            )
            return null
        }
    }

    /**
     * Executes the testNodes in [TestGroupNode] in parallel and returns the result.
     */
    private suspend fun executeTestGroup(data: TestGroupNode): ResultGroupNode = coroutineScope {
        val tests = data.testNodes
        val deferredResults = mutableListOf<Deferred<ResultNode>>()

        messageBus.syncPublisher(JudgingProcessListener.TOPIC).onTestGroupStarted(data)

        for (test in tests)
            deferredResults += async {
                messageBus.syncPublisher(JudgingProcessListener.TOPIC).onTestNodeStarted(test)
                val result = executeTestNode(test)
                messageBus.syncPublisher(JudgingProcessListener.TOPIC).onTestNodeFinished(result)
                result
            }

        val results = awaitAll(*deferredResults.toTypedArray())

        val resultGroupNode = ResultGroupNode(data, results)

        messageBus.syncPublisher(JudgingProcessListener.TOPIC).onTestGroupFinished(resultGroupNode)

        return@coroutineScope resultGroupNode
    }

    /**
     * Executes the [TestNode] and returns the result.
     */
    private suspend fun executeTestNode(data: TestNode): ResultNode {
        val out = withContext(Dispatchers.IO) {
            ExecutionUtil.execAndGetOutput(
                data.buildOutput.executeCommand,
                data.buildOutput.dir,
                data.input,
                data.timeLimit
            )
        }

        val verdict = when {
            out.isTimeout -> Verdict.TIME_LIMIT_EXCEEDED
            out.isCancelled -> Verdict.CANCELLED
            out.stdout.trimByLines().contentEquals(data.expectedOutput.trimByLines()) -> Verdict.CORRECT_ANSWER
            out.exitCode != 0 -> Verdict.RUNTIME_ERROR
            else -> Verdict.WRONG_ANSWER
        }

        return ResultNode(data, out, verdict)
    }
}