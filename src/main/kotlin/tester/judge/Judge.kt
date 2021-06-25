package tester.judge

import tester.base.ProcessRunner
import tester.tree.TestNode

object Judge {
    fun produceVerdict(node: TestNode.Leaf, result: ProcessRunner.CapturedResults): Pair<Verdict, String> {
        if (result.error.isNotEmpty())
            return Pair(Verdict.RUNTIME_ERROR, "")

        val expectedOutput = node.expectedOutput.trim()
        val actualOutput = result.output.trim().replace("\r", "")

        return if (!actualOutput.contentEquals(expectedOutput)) {
            Pair(Verdict.WRONG_ANSWER, "Expected Output:\n${node.expectedOutput}")
        } else
            Pair(Verdict.CORRECT_ANSWER, "")
    }
}