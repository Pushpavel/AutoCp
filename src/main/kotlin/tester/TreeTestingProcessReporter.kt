package tester

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import common.errors.Err
import tester.tree.ResultNode
import tester.tree.TestNode
import tester.tree.TreeTestingProcess

class TreeTestingProcessReporter(private val processHandler: ProcessHandler) : TreeTestingProcess.Listener {

    override fun leafStart(node: TestNode.Leaf) {
        TODO("Not yet implemented")
    }

    override fun leafFinish(node: ResultNode.Leaf) {
        TODO("Not yet implemented")
    }

    override fun groupStart(node: TestNode.Group) {
        TODO("Not yet implemented")
    }

    override fun groupFinish(node: ResultNode.Group) {
        TODO("Not yet implemented")
    }

    override fun testingProcessStartErrored(error: Err) {
        TODO("Not yet implemented")
    }

    private fun ServiceMessageBuilder.apply() {
        processHandler.notifyTextAvailable(
            this.toString() + "\n", ProcessOutputTypes.STDOUT
        )
    }
}