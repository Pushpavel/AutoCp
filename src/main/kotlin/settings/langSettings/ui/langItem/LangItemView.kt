package settings.langSettings.ui.langItem

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.ui.fullRow
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanel
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import kotlinx.coroutines.launch
import settings.langSettings.model.BuildConfig
import ui.StringCellRenderer
import ui.helpers.viewScope
import ui.vvm.swingBinding.bind
import ui.vvm.swingModels.collectionListModel
import ui.vvm.swingModels.singleSelectionModel
import java.awt.BorderLayout
import javax.swing.JPanel

class LangItemView(viewModel: LangItemViewModel) : JBPanel<LangItemView>(BorderLayout()) {
    val scope = viewScope(viewModel.scope)
    val configComboBox = ComboBox<BuildConfig>()
    val fileTemplateComboBox = ComboBox<FileTemplate>()

    val list = JBList<BuildConfig>()
    val listContainer: JPanel
    private val container: DialogPanel

    init {
        list.cellRenderer = BuildConfig.cellRenderer()

        listContainer = ToolbarDecorator.createDecorator(list).setAddAction {
            viewModel.addNewConfig()
        }.setEditAction {
            viewModel.editConfig()
        }.createPanel()

        configComboBox.renderer = BuildConfig.cellRenderer()


        fileTemplateComboBox.renderer = StringCellRenderer<FileTemplate> {
            Pair(it.name, FileTypeManager.getInstance().getFileTypeByExtension(it.extension).icon)
        }

        container = panel {
            row("File Template") {
                fileTemplateComboBox()
            }
            row("Default Build Configuration") {
                configComboBox()
            }
            titledRow("Build Configurations") {
                subRowIndent = 0
                fullRow {
                    listContainer(CCFlags.grow)
                }
            }
        }

        add(container, BorderLayout.CENTER)

        scope.launch {
            list.model = collectionListModel(
                viewModel.buildConfigs,
                viewModel.buildConfigs
            )

            list.selectionModel = singleSelectionModel(viewModel.selectedConfigIndex)

            bind(
                configComboBox,
                viewModel.buildConfigs,
                viewModel.defaultBuildConfigIndex
            )

            bind(
                fileTemplateComboBox,
                viewModel.fileTemplates,
                viewModel.selectedFileTemplateIndex
            )
        }
    }
}