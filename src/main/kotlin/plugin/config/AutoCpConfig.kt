package plugin.config

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element
import plugin.ui.ConfigEditor

class AutoCpConfig(project: Project, factory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase<RunProfileState>(project, factory, name) {
    companion object {
        private const val PROBLEM_NAME = "problemName"
        private const val EXEC_PATH = "execPath"
    }

    var problemName: String = ""
    var executablePath: String = ""

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        if (problemName.isEmpty() && executablePath.isEmpty())
            return null
        return AutoCpRunState(this)
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return ConfigEditor(project)
    }

    override fun suggestedName(): String? {
        if (problemName.isEmpty())
            return null
        return problemName
    }

    override fun writeExternal(element: Element) {
        JDOMExternalizerUtil.writeField(element, PROBLEM_NAME, problemName)
        JDOMExternalizerUtil.writeField(element, EXEC_PATH, executablePath)
        super.writeExternal(element)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        problemName = JDOMExternalizerUtil.readField(element, PROBLEM_NAME, "")
        executablePath = JDOMExternalizerUtil.readField(element, EXEC_PATH, "")
    }

}