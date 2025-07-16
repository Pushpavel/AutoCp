package com.github.pushpavel.autocp.tester.base

import com.github.pushpavel.autocp.database.models.JudgeSettings
import com.github.pushpavel.autocp.database.models.Testcase
import com.github.pushpavel.autocp.tester.errors.TestcaseJudgingErr
import com.github.pushpavel.autocp.tester.errors.Verdict
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class Judge(private val judge: ProcessRunner, private val settings: JudgeSettings) {

    suspend fun judgeTestcase(testcase: Testcase, participant: ProcessRunner): Verdict {
        judge.reset()
        return if (settings.isInteractive)
            judgeInteractiveTestcase(testcase, participant)
        else
            judgeNormalTestcase(testcase, participant)
    }

    private suspend fun judgeNormalTestcase(testcase: Testcase, participant: ProcessRunner): Verdict {
        val performance = participant
            .setInput(testcase.input, settings.inputFile)
            .registerOutput("answer", settings.outputFile)
            .run(settings.timeLimit)
        if (performance.exitCode != 0)
            return Verdict.RuntimeErr(
                performance["answer"] ?: "",
                "Solution exited with invalid exit code ${performance.exitCode}"
            )
        val judged = judge
            .setInput(testcase.input, "input.txt")
            .setInput(testcase.output ?: "", "correct.txt")
            .setInput(performance["answer"] ?: "", "answer.txt")
            .registerOutput("comment", "comment.txt")
            .run()
        return when (judged.exitCode) {
            0 -> Verdict.CorrectAnswer(performance["answer"] ?: "", performance.executionTime, judged["comment"])
            1 -> Verdict.WrongAnswer(testcase.output, performance["answer"] ?: "", performance.executionTime, judged["comment"])
            else -> throw TestcaseJudgingErr.JudgeFailed(performance["answer"] ?: "", "Judge exited with invalid exit code ${judged.exitCode}", judged.exitCode)
        }
    }

    private suspend fun judgeInteractiveTestcase(testcase: Testcase, participant: ProcessRunner) = coroutineScope {
        val output = StringBuilder()
        judge.addOutputListener { participant.write(it) }
        participant.addOutputListener { judge.write(it) }
        judge.addOutputListener { output.append("judge -> participant: $it") }
        participant.addOutputListener { output.append("participant -> judge: $it") }
        judge
            .setInput(testcase.input, "input.txt")
            .setInput(testcase.output ?: "", "correct.txt")
            .registerOutput("comment", "comment.txt")
        val participantJob = async { participant.run(settings.timeLimit) }
        val judgeJob = async { judge.run() }
        judgeJob.invokeOnCompletion { participant.kill() }
        participantJob.invokeOnCompletion { judge.terminateInput() }
        val (performance, judged) = awaitAll(participantJob, judgeJob)
        return@coroutineScope when (judged.exitCode) {
            0 -> Verdict.CorrectAnswer(output.toString(), performance.executionTime, judged["comment"])
            1 -> Verdict.WrongAnswer(testcase.output, output.toString(), performance.executionTime, judged["comment"])
            else -> throw TestcaseJudgingErr.JudgeFailed(output.toString(), "Judge exited with invalid exit code ${judged.exitCode}", judged.exitCode)
        }
    }

    fun shouldUseJudge(testcase: Testcase): Boolean {
        return testcase.output == null || settings.preferJudgeOverOutput
    }

}
