package plugin.config

import com.google.common.io.Files
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element
import plugin.ui.RunConfigSettings

class AutoCpConfig(project: Project, factory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase<RunProfileState>(project, factory, name) {
    companion object {
        private const val SOLUTION_FILE_PATH = "solutionFilePath"
        private const val EXEC_PATH = "execPath"
    }

    var solutionFilePath: String = ""
    var executablePath: String = ""

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        if (solutionFilePath.isEmpty() && executablePath.isEmpty())
            return null
        return AutoCpRunState(this)
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return RunConfigSettings(project)
    }

    override fun suggestedName(): String? {
        if (solutionFilePath.isEmpty())
            return null
        return Files.getNameWithoutExtension(solutionFilePath)
    }

    override fun writeExternal(element: Element) {
        JDOMExternalizerUtil.writeField(element, SOLUTION_FILE_PATH, solutionFilePath)
        JDOMExternalizerUtil.writeField(element, EXEC_PATH, executablePath)
        super.writeExternal(element)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        solutionFilePath = JDOMExternalizerUtil.readField(element, SOLUTION_FILE_PATH, "")
        executablePath = JDOMExternalizerUtil.readField(element, EXEC_PATH, "")
    }

}