package com.github.pushpavel.autocp.tool.ui

import com.github.pushpavel.autocp.common.helpers.mainScope
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.common.ui.helpers.allowOnlyPositiveIntegers
import com.github.pushpavel.autocp.common.ui.helpers.onChange
import com.github.pushpavel.autocp.common.ui.layouts.html
import com.github.pushpavel.autocp.common.ui.layouts.tag
import com.github.pushpavel.autocp.database.SolutionFiles
import com.github.pushpavel.autocp.database.models.SolutionFile
import com.intellij.openapi.Disposable
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.BottomGap
import com.intellij.ui.dsl.builder.bindIntText
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.components.BorderLayoutPanel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.awt.BorderLayout
import kotlin.io.path.Path

class SolutionFileSettingsPanel(project: Project, pathString: String, refreshCallback: () -> Unit) :
    Disposable {

    private val solutionFiles = SolutionFiles.getInstance(project)
    private val flow = solutionFiles.listenFlow(pathString)

    private var solutionFile: SolutionFile? = null
    var timeLimit = 0
    private var resetting = false

    private val scope = mainScope()

    private val header: DialogPanel = panel {
        indent {
            row {
                val icon = label("").component
                val title = label(html { tag("h2", "...") }).component

                scope.launch {
                    flow.filterNotNull().collect {
                        val fileName = Path(it.pathString).fileName.toString()
                        // icon of the file type of the solution file
                        icon.icon = FileTypeManager.getInstance()
                            .getFileTypeByFileName(fileName).icon

                        title.text = html { tag("h2", fileName) }
                    }
                }
            }
        }
    }


    private val body: DialogPanel = panel {
        indent {

            row { label("Constraints:") }
            row {
                panel {
                    row {

                        label("").applyToComponent { icon = R.icons.clock }

                        intTextField().bindIntText(::timeLimit).applyToComponent {
                            allowOnlyPositiveIntegers()
                            document.onChange { apply() }
                        }
                        label("ms")
                    }
                }
            }.bottomGap(BottomGap.MEDIUM)
            row {
                button("Remove All Testcases") {
                    solutionFile?.pathString?.let { it1 -> solutionFiles.remove(it1) }
                    refreshCallback()
                }
            }
        }
    }

    val component = BorderLayoutPanel().apply {
        add(header, BorderLayout.PAGE_START)
        add(body, BorderLayout.CENTER)
    }

    init {
        scope.launch { flow.collect { reset(it) } }
    }

    fun apply() {
        if (resetting) return

        solutionFile?.let {
            header.apply()
            body.apply()

            solutionFile = it.copy(timeLimit = timeLimit.toLong())

            solutionFiles.update(solutionFile!!)
        }
    }

    private fun reset(solutionFile: SolutionFile?) {
        resetting = true
        this.solutionFile = solutionFile

        timeLimit = solutionFile?.timeLimit?.toInt() ?: 0

        header.reset()
        body.reset()
        resetting = false
    }

    override fun dispose() = scope.cancel()
}