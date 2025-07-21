package com.github.pushpavel.autocp.config.stress

import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.config.AutoCpConfig
import com.github.pushpavel.autocp.database.SolutionFiles
import com.github.pushpavel.autocp.database.autoCp
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializer
import org.jdom.Element
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

class AutoCpStressConfig(project: Project, factory: ConfigurationFactory, name: String):
    AutoCpConfig(project, factory, name) {

    private val solutionFiles = SolutionFiles.getInstance(project)

    override fun getState(executor: Executor, environment: ExecutionEnvironment) = AutoCpStressRunState(project, this)

    override fun getConfigurationEditor() = StressConfigEditor(project, this)

    override fun suggestedName(): String? {
        if (solutionFilePath.isEmpty())
            return null

        if (solutionFilePath in solutionFiles) {
            val id = solutionFiles[solutionFilePath]!!.linkedProblemId
            if (id != null) {
                val name = project.autoCp().problems[id.first]?.get(id.second)?.name
                if (name != null)
                    return R.strings.stressConfigNamePrefix + name
            }
        }

        return R.strings.stressConfigNamePrefix + Path(solutionFilePath).nameWithoutExtension
    }

    override fun writeExternal(element: Element) {
        XmlSerializer.serializeInto(this, element)
        super.writeExternal(element)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        XmlSerializer.deserializeInto(this, element)
    }

}