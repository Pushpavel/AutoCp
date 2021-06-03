package tester.spec

import tester.run.ProgramExecutorFactory

open class BaseSpec(
    val name: String,
    private val programExecutorFactory: ProgramExecutorFactory?,
) {
    private var parentSpec: BaseSpec? = null

    fun getProgramFactory(): ProgramExecutorFactory {
        if (programExecutorFactory != null)
            return programExecutorFactory

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