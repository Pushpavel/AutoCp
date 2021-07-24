package gather.ui.solutionsDialog

import com.github.pushpavel.autocp.database.Problem
import com.intellij.ide.ui.fullRow
import com.intellij.lang.Language
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import kotlinx.coroutines.cancel
import settings.langSettings.model.Lang
import ui.StringCellRenderer
import ui.helpers.mainScope
import ui.layouts.SingleChildContainer
import java.awt.Dimension

class SolutionsDialog(val model: SolutionsDialogModel) : DialogWrapper(false) {

    val scope = mainScope()

    init {
        title = "Generate Solution Files"
        init()
    }

    override fun createCenterPanel() = SingleChildContainer("No Problems to Generate Files", panel {
        model.groupName?.let {
            titledRow(model.groupName) {
                subRowIndent = 0
                val problemList = JBList(model.listModel)
                val langComboBox = ComboBox(model.langModel)

                fullRow {
                    problemList.cellRenderer = StringCellRenderer<Problem> {
                        Pair(it.name, model.langIcon)
                    }
                    ToolbarDecorator
                        .createDecorator(problemList)
                        .disableUpDownActions()
                        .disableRemoveAction()
                        .createPanel()(CCFlags.grow)

                }

                fullRow {
                    langComboBox.renderer = Lang.cellRenderer()
                    langComboBox()
                    model.langModel.selected?.let {
                        val lang = Language.findLanguageByID(it.langId)
                        model.langIcon = lang?.associatedFileType?.icon
                        problemList.updateUI()
                    }

                    langComboBox.addActionListener { _ ->
                        model.langModel.selected?.let {
                            val lang = Language.findLanguageByID(it.langId)
                            model.langIcon = lang?.associatedFileType?.icon
                            problemList.updateUI()
                        }
                    }
                }
            }
        }

    }).apply {
        setChildVisible(model.groupName != null)
        minimumSize = Dimension(300, 300)
    }

    override fun dispose() {
        super.dispose()
        scope.cancel()
    }
}