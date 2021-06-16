package config

import com.google.common.io.Files
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element

class AutoCpConfig(project: Project, factory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase<RunProfileState>(project, factory, name) {
    companion object {
        private const val SOLUTION_FILE_PATH = "solutionFilePath"
        private const val SOLUTION_LANG_ID = "solutionLang"
    }

    var solutionFilePath: String = ""

    @Deprecated("should be removed after tester package refactored")
    var executablePath: String = ""
    var solutionLangId: Long = -1

    override fun getState(executor: Executor, environment: ExecutionEnvironment) = AutoCpRunState(this)

    override fun getConfigurationEditor() = ConfigEditor(project)

    override fun suggestedName(): String? {
        if (solutionFilePath.isEmpty())
            return null
        return Files.getNameWithoutExtension(solutionFilePath)
    }

    override fun writeExternal(element: Element) {
        JDOMExternalizerUtil.writeField(element, SOLUTION_FILE_PATH, solutionFilePath)
        JDOMExternalizerUtil.writeField(element, SOLUTION_LANG_ID, solutionLangId.toString())
        super.writeExternal(element)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        solutionFilePath = JDOMExternalizerUtil.readField(element, SOLUTION_FILE_PATH, "")
        solutionLangId = JDOMExternalizerUtil.readField(element, SOLUTION_LANG_ID, "-1").toLong()
    }

}