package tester

import config.AutoCpConfig
import tester.base.TestingProcess
import tester.base.TestingProcessHandler

class AutoCpTestingProcessHandler(val config: AutoCpConfig) : TestingProcessHandler() {
    override fun createTestingProcess(): TestingProcess<Unit> {
        // Get and Validate Problem from config
        // Build Test tree
        // create a TestingProcess from the Problem and Test Tree
        TODO()
    }
}