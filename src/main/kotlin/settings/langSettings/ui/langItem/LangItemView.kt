package settings.langSettings.ui.langItem

import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanel
import common.diff.DiffAdapter
import kotlinx.coroutines.CoroutineScope
import settings.langSettings.model.BuildConfig
import ui.StringCellRenderer
import ui.vvm.View
import ui.vvm.swingModels.toCollectionListModel
import ui.vvm.swingModels.toSingleSelectionModel
import java.awt.BorderLayout

class LangItemView : JBPanel<LangItemView>(BorderLayout()), View<LangItemViewModel> {

    val list = JBList<BuildConfig>().apply {
        cellRenderer = StringCellRenderer<BuildConfig> { it.name }
    }

    override fun CoroutineScope.onViewModelBind(viewModel: LangItemViewModel) {
        val container = ToolbarDecorator.createDecorator(list).setAddAction {
            viewModel.addNewConfig()
        }.setEditAction {
            viewModel.editConfig()
        }.createPanel()

        list.model = viewModel.buildConfig.toCollectionListModel(
            this,
            viewModel.buildConfigChanges,
            object : DiffAdapter<BuildConfig> {
                override fun isSame(item1: BuildConfig, item2: BuildConfig) = item1.name == item2.name
            })

        list.selectionModel = viewModel.selectedConfigIndex.toSingleSelectionModel(this, viewModel.selectedConfigIndex)

        add(container, BorderLayout.CENTER)
    }

}