package settings.langSettings.ui.langItem

import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanel
import kotlinx.coroutines.CoroutineScope
import settings.langSettings.model.BuildConfig
import ui.vvm.View
import ui.vvm.swingModels.collectionListModel
import ui.vvm.swingModels.singleSelectionModel
import java.awt.BorderLayout

class LangItemView : JBPanel<LangItemView>(BorderLayout()), View<LangItemViewModel> {

    val list = JBList<BuildConfig>().apply {
        cellRenderer = BuildConfig.cellRenderer()
    }

    override fun CoroutineScope.onViewModelBind(viewModel: LangItemViewModel) {
        val container = ToolbarDecorator.createDecorator(list).setAddAction {
            viewModel.addNewConfig()
        }.setEditAction {
            viewModel.editConfig()
        }.createPanel()

        list.model = collectionListModel(
            viewModel.buildConfig,
            viewModel.buildConfigChanges
        ) { item1, item2 -> item1.name == item2.name }

        list.selectionModel = singleSelectionModel(viewModel.selectedConfigIndex)

        add(container, BorderLayout.CENTER)
    }

}