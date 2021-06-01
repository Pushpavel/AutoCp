package config

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element

class AutoCpConfig(project: Project, factory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase<RunProfileState>(project, factory, name) {
    companion object {
        private const val PROBLEM_NAME = "problemName"
        private const val RUN_COMMAND = "runCommand"
    }

    var problemName: String = ""
    var runCommand: String = ""

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        if (problemName.isEmpty() && runCommand.isEmpty())
            return null
        return AutoCpRunState(this)
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return AutoCpConfigSettingsEditor(project)
    }

    override fun suggestedName(): String? {
        if (problemName.isEmpty())
            return null
        return problemName
    }

    override fun writeExternal(element: Element) {
        JDOMExternalizerUtil.writeField(element, PROBLEM_NAME, problemName)
        JDOMExternalizerUtil.writeField(element, RUN_COMMAND, runCommand)
        super.writeExternal(element)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        problemName = JDOMExternalizerUtil.readField(element, PROBLEM_NAME, "")
        runCommand = JDOMExternalizerUtil.readField(element, RUN_COMMAND, "")
    }

}