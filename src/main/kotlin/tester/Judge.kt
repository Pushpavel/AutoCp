package tester

import tester.base.ProcessRunner
import tester.models.ResultCode
import tester.tree.TestNode

object Judge {
    fun produceVerdict(node: TestNode.Leaf, result: ProcessRunner.CapturedResults): Pair<ResultCode, String> {
        if (result.error.isNotEmpty())
            return Pair(ResultCode.PROGRAM_CRASH, "")

        val expectedOutput = node.expectedOutput.trim()
        val actualOutput = result.output.trim().replace("\r", "")

        return if (!actualOutput.contentEquals(expectedOutput)) {
            Pair(ResultCode.WRONG_ANSWER, "Expected Output:\n${node.expectedOutput}")
        } else
            Pair(ResultCode.CORRECT_ANSWER, "")
    }
}