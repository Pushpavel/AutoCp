package tool.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.ui.layout.applyToComponent
import com.intellij.ui.layout.panel
import common.helpers.mainScope
import common.res.R
import common.ui.helpers.allowOnlyPositiveIntegers
import common.ui.layouts.html
import common.ui.layouts.tag
import database.autoCp
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.swing.JComponent
import kotlin.io.path.Path

class SolutionFileSettingsPanel(project: Project, private val pathString: String) : Disposable {

    private val db = project.autoCp()
    private val flow = db.solutionFilesFlow.map { it[pathString] }.filterNotNull()

    private val scope = mainScope()
    val component: JComponent

    init {
        component = panel {
            row {
                placeholder().constraints(growX, pushX)
                cell {
                    val icon = label("").component
                    val title = label("").component

                    scope.launch {
                        flow.collect {
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
            row {
                row { label("Constraints:") }
                row {
                    cell {
                        label("").applyToComponent { icon = R.icons.clock }
                        intTextField({ 3 }, {}).applyToComponent {
                            allowOnlyPositiveIntegers()
                        }
                        label("ms")
                    }
                    placeholder().constraints(growX, pushX)
                }
                row {
                    cell {
                        label("").applyToComponent { icon = R.icons.memory }
                        intTextField({ 3 }, {}).applyToComponent {
                            allowOnlyPositiveIntegers()
                        }
                        label("MB")
                    }
                    placeholder().constraints(growX, pushX)
                }
            }
        }
    }

    override fun dispose() = scope.cancel()
}