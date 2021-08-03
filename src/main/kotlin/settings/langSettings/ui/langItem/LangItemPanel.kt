package settings.langSettings.ui.langItem

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.ui.fullRow
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.SingleSelectionModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.InnerCell
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import common.isItemsEqual
import lang.supportedFileTemplates
import settings.langSettings.model.BuildConfig
import settings.langSettings.model.Lang
import ui.dsl.DslCallbacks
import ui.dsl.comboBoxView
import ui.swing.TileCellRenderer

class LangItemPanel : DslCallbacks {

    var selectedLang: Lang? = null

    private val fileTemplatesModel = CollectionComboBoxModel<FileTemplate>()
    private val buildConfigsModel = CollectionComboBoxModel<BuildConfig>()

    val dialogPanel = panel(LCFlags.fill) {
        row("File Template") {
            comboBoxView(
                fileTemplatesModel,
                { it.name == selectedLang?.fileTemplateName },
                {
                    selectedLang?.apply {
                        selectedLang = copy(fileTemplateName = it?.name)
                    }
                },
                TileCellRenderer {
                    text = it.name
                    icon = FileTypeManager.getInstance().getFileTypeByExtension(it.extension).icon
                }
            )
        }

        row("Default Build Configuration") {
            comboBoxView(
                buildConfigsModel,
                { it.id == selectedLang?.defaultBuildConfigId },
                {
                    selectedLang?.apply {
                        selectedLang = copy(defaultBuildConfigId = it?.id)
                    }
                },
                BuildConfig.cellRenderer()
            )
        }

        titledRow("Build Configurations") {
            subRowIndent = 0
            fullRow {
                buildConfigsList()
            }
        }
    }

    private fun InnerCell.buildConfigsList() {
        val jbList = JBList(buildConfigsModel).apply {
            selectionModel = SingleSelectionModel()
            cellRenderer = BuildConfig.cellRenderer()
        }

        val listContainer = ToolbarDecorator.createDecorator(jbList).setAddAction {
//                        viewModel.addNewConfig()
        }.setEditAction {
//                        viewModel.editConfig()
        }.createPanel()


        listContainer(CCFlags.grow, CCFlags.push)
    }

    override fun isModified(): Boolean {
        return dialogPanel.isModified() || !buildConfigsModel.items.isItemsEqual(selectedLang?.buildConfigs ?: listOf())
    }

    override fun reset() {
        val selectedLang = selectedLang
        if (selectedLang == null) {
            fileTemplatesModel.removeAll()
            buildConfigsModel.removeAll()
        } else {
            fileTemplatesModel.replaceAll(selectedLang.supportedFileTemplates())
            buildConfigsModel.replaceAll(selectedLang.buildConfigs)
        }

        dialogPanel.reset()
    }

    override fun apply() {
        selectedLang?.apply {
            selectedLang = copy(buildConfigs = buildConfigsModel.items)
        }

        dialogPanel.apply()
    }
}