package tool.ui

import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.ui.layout.applyToComponent
import com.intellij.ui.layout.panel
import common.res.R
import common.ui.helpers.allowOnlyPositiveIntegers
import common.ui.layouts.html
import common.ui.layouts.tag
import javax.swing.JComponent

class SolutionFileSettingsPanel : Disposable {

    val component: JComponent

    init {
        component = panel {
            row {
                placeholder().constraints(growX, pushX)
                cell {
                    label("").applyToComponent { icon = AllIcons.FileTypes.Config } // TODO: get Language's icon
                    label(html {
                        tag("h2", "A. Abomination Police Dog")
                    })
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

    override fun dispose() {}
}