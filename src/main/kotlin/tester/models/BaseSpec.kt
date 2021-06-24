package tester.models

import tester.run.ExecutableRunnerFactory

@Deprecated("")
open class BaseSpec(
    val name: String,
    private val executableRunnerFactory: ExecutableRunnerFactory?,
) {
    private var parentSpec: BaseSpec? = null

    fun getProgramFactory(): ExecutableRunnerFactory {
        if (executableRunnerFactory != null)
            return executableRunnerFactory

        val factory = parentSpec?.getProgramFactory()

        if (factory != null)
            return factory

        throw IllegalStateException("ProgramExecutorFactory Not Found")
    }

    fun setParent(parent: BaseSpec) {
        if (parentSpec != null)
            throw IllegalStateException("parentSpec already defined")
        parentSpec = parent
    }
}