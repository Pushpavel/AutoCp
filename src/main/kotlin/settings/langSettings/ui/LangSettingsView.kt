package settings.langSettings.ui

import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import settings.langSettings.model.Lang
import ui.layouts.SingleChildContainer
import ui.vvm.View
import ui.vvm.swingModels.toSingleSelectionModel

class LangSettingsView : OnePixelSplitter(false, 0.3F), View<LangSettingsViewModel> {

    private val sideList: JBList<Lang> = JBList<Lang>()
    private val itemContainer: SingleChildContainer

    init {

        val listContainer = ToolbarDecorator.createDecorator(sideList).createPanel()

        itemContainer = SingleChildContainer("Select a Language", JBLabel("Amazing work"))

        firstComponent = listContainer
        secondComponent = itemContainer

    }


    override fun CoroutineScope.onViewModelBind(viewModel: LangSettingsViewModel) {
        sideList.selectionModel = viewModel.selectedLangIndex.toSingleSelectionModel(this)

        launch {
            viewModel.selectedLangIndex.collect {
                itemContainer.setChildVisible(it != -1)
            }
        }
    }
}