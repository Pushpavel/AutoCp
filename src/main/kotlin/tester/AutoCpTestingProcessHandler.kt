package tester

import config.AutoCpConfig
import tester.base.TestingProcess
import tester.base.TestingProcessHandler

class AutoCpTestingProcessHandler(val config: AutoCpConfig) : TestingProcessHandler() {
    override fun createTestingProcess(): TestingProcess<Unit> {
        TODO()
    }
}