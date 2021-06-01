package run

import com.intellij.openapi.Disposable
import common.AutoCpProblem

class RunProblemTests(problem: AutoCpProblem, runCommand: String, reporter: TestProcessReporter) : Disposable {
    var runningTestCase: RunTestCase? = null
    var stopped: Boolean = false

    init {
        reporter.problemStarted(problem)
        for (testcase in problem.tests) {
            if (stopped) continue
            runningTestCase = RunTestCase(runCommand, testcase, reporter)
        }
        reporter.problemFinished(problem)
    }

    override fun dispose() {
        runningTestCase?.dispose()
        stopped = true
    }


}