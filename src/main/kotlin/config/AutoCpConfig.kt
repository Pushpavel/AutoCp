package config

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import common.Constants
import org.jdom.Element

class AutoCpConfig(project: Project, factory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase<RunProfileState>(project, factory, name) {

    companion object {
        private const val SOLUTION_FILE_PATH = "solutionFilePath"
    }

    var solutionFilePath: String = ""

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        if (solutionFilePath.isEmpty())
            return null
        return AutoCpRunState(this)
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return AutoCpConfigSettingsEditor(project)
    }

    override fun suggestedName(): String? {
        if (solutionFilePath.isEmpty())
            return null
        return solutionFilePath
    }

    override fun writeExternal(element: Element) {
        JDOMExternalizerUtil.writeField(element, SOLUTION_FILE_PATH, solutionFilePath)
    }

    override fun readExternal(element: Element) {
        solutionFilePath = JDOMExternalizerUtil.readField(element, SOLUTION_FILE_PATH, "")
    }

}