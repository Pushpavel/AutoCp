package run

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.openapi.components.service
import plugin.config.AutoCpConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import plugin.services.AutoCpFilesService
import java.io.OutputStream

class TestProcessHandler(config: AutoCpConfig) : ProcessHandler() {
    private val service = config.project.service<AutoCpFilesService>()
    private var runner: RunProblemTests? = null

    init {
        GlobalScope.launch {
            val problem = service.getAutoCp(config.problemName)!!

            if (problem == null) {
                notifyTextAvailable(
                    "Problem \"${config.problemName}\" is not yet parsed. run Gather Problems action to parse them",
                    ProcessOutputTypes.STDERR
                )
                destroyProcess()
            } else {
                val reporter = TestProcessReporter(this@TestProcessHandler)
                reporter.startedTesting()
                runner = RunProblemTests(problem, config.runCommand, reporter)
                destroyProcess()
            }
        }
    }

    override fun destroyProcessImpl() {
        print("destroying Process\n")
        runner?.dispose()
        notifyProcessTerminated(0)
    }

    override fun detachProcessImpl() {
        notifyProcessDetached()
        destroyProcess() // don't know if this should be here
    }

    override fun detachIsDefault() = false

    override fun getProcessInput(): OutputStream? {
        print("\nasking for getProcessInput\n")
        return null
    }

}