package tool.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.applyToComponent
import com.intellij.ui.layout.panel
import com.intellij.util.ui.components.BorderLayoutPanel
import common.helpers.mainScope
import common.res.R
import common.ui.helpers.allowOnlyPositiveIntegers
import common.ui.helpers.onChange
import common.ui.layouts.html
import common.ui.layouts.tag
import database.autoCp
import database.models.SolutionFile
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.awt.BorderLayout
import kotlin.io.path.Path

class SolutionFileSettingsPanel(project: Project, private val pathString: String) : Disposable {

    private val db = project.autoCp()
    private val flow = db.solutionFilesFlow.map { it[pathString] }

    private var solutionFile: SolutionFile? = null
    var timeLimit = 0
    var memoryLimit = 0
    var resetting = false

    private val scope = mainScope()

    private val header: DialogPanel = panel {
        row {
            placeholder().constraints(growX, pushX)
            cell {
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
            placeholder().constraints(growX, pushX)
        }
    }


    private val body: DialogPanel = panel {
        row {
            row { label("Constraints:") }
            row {
                cell {
                    label("").applyToComponent { icon = R.icons.clock }
                    intTextField(::timeLimit).applyToComponent {
                        allowOnlyPositiveIntegers()
                        document.onChange { apply() }
                    }
                    label("ms")
                }
                placeholder().constraints(growX, pushX)
            }
            row {
                cell {
                    label("").applyToComponent { icon = R.icons.memory }
                    intTextField(::memoryLimit).applyToComponent {
                        allowOnlyPositiveIntegers()
                        document.onChange { apply() }
                    }
                    label("MB")
                }
                placeholder().constraints(growX, pushX)
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

            solutionFile = it.copy(
                timeLimit = timeLimit.toLong(),
                memoryLimit = memoryLimit.toLong()
            )

            db.updateSolutionFile(solutionFile!!)
        }
    }

    private fun reset(solutionFile: SolutionFile?) {
        resetting = true
        this.solutionFile = solutionFile

        solutionFile?.let {
            memoryLimit = it.memoryLimit.toInt()
            timeLimit = it.timeLimit.toInt()
        } ?: run {
            memoryLimit = 0
            timeLimit = 0
        }


        header.reset()
        body.reset()
        resetting = false
    }

    override fun dispose() = scope.cancel()
}