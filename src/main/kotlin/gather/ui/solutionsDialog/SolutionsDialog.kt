package gather.ui.solutionsDialog

import com.github.pushpavel.autocp.database.Problem
import com.intellij.ide.ui.fullRow
import com.intellij.lang.Language
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.CollectionListModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import settings.generalSettings.AutoCpGeneralSettings
import settings.langSettings.AutoCpLangSettings
import settings.langSettings.model.Lang
import ui.dsl.simpleComboBoxView
import ui.helpers.onSelectedItem
import ui.swing.TileCellRenderer
import java.awt.Dimension
import javax.swing.Icon

class SolutionsDialog(private val groupName: String, problems: List<Problem>) : DialogWrapper(false) {

    private val solutionListModel = CollectionListModel(problems.toMutableList())
    var langIcon: Icon? = null

    private val problemList = JBList(solutionListModel).apply {
        cellRenderer = TileCellRenderer {
            text = it.name
            icon = langIcon
        }
    }
    private lateinit var langComboBox: ComboBox<Lang>

    init {
        title = "Generate Solution Files"
        isOKActionEnabled = true
        init()
    }

    override fun createCenterPanel() = panel {
        titledRow(groupName) {
            subRowIndent = 0


            fullRow {
                ToolbarDecorator
                    .createDecorator(problemList)
                    .disableUpDownActions()
                    .disableRemoveAction()
                    .createPanel()(CCFlags.grow)
            }

            fullRow {
                langComboBox = simpleComboBoxView(
                    AutoCpLangSettings.instance.languages,
                    { it.langId == AutoCpGeneralSettings.instance.preferredLangId },
                    {}, Lang.cellRenderer()
                ).component
            }
        }
    }.apply {
        minimumSize = Dimension(300, 300)

        langComboBox.onSelectedItem {
            isOKActionEnabled = it != null
            val lang = Language.findLanguageByID(it?.langId)
            langIcon = lang?.associatedFileType?.icon
            problemList.updateUI()
        }
    }

    fun showAndGetLang(): Lang? {
        val confirm = showAndGet()

        return if (confirm)
            langComboBox.selectedItem as Lang?
        else
            null
    }

}