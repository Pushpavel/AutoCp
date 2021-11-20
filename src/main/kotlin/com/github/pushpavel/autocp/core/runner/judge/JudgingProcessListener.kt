package com.github.pushpavel.autocp.core.runner.judge

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.core.execution.BuildOutput
import com.github.pushpavel.autocp.core.persistance.solutions.Solution
import com.github.pushpavel.autocp.core.persistance.testcases.Testcase
import com.intellij.util.messages.Topic

interface JudgingProcessListener {
    companion object {
        val TOPIC = Topic.create("JudgingProcessListener", JudgingProcessListener::class.java)
    }

    fun onBuildingStarted(solutionPath: String, lang: Lang, buildCommand: String)
    fun onTestingStarted(solution: Solution, testcases: List<Testcase>, buildOutput: BuildOutput)
    fun onTestingFailed(err: JudgeErr)
    fun onTestingFinished()
    fun onTestNodeStarted(testNode: TestNode)
    fun onTestNodeFinished(result: ResultNode)
    fun onTestGroupStarted(tests: TestGroupNode)
    fun onTestGroupFinished(results: ResultGroupNode)
}