package settings.langSettings.ui.langItem

import com.intellij.ide.ui.fullRow
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanel
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import kotlinx.coroutines.launch
import settings.langSettings.model.BuildConfig
import ui.helpers.viewScope
import ui.vvm.swingModels.collectionListModel
import ui.vvm.swingModels.singleSelectionModel
import java.awt.BorderLayout

class LangItemView(viewModel: LangItemViewModel) : JBPanel<LangItemView>(BorderLayout()) {
    val scope = viewScope(viewModel.scope)

    init {
        val list = JBList<BuildConfig>()
        list.cellRenderer = BuildConfig.cellRenderer()

        val listContainer = ToolbarDecorator.createDecorator(list).setAddAction {
            viewModel.addNewConfig()
        }.setEditAction {
            viewModel.editConfig()
        }.createPanel()

        val container = panel {
            row("Default Build Configuration") {
                ComboBox<String>()()
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
                viewModel.buildConfig,
                viewModel.buildConfigChanges
            ) { item1, item2 -> item1.name == item2.name }

            list.selectionModel = singleSelectionModel(viewModel.selectedConfigIndex)
        }
    }
}