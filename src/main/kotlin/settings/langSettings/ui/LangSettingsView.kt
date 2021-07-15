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
import ui.vvm.swingModels.toCollectionListModel
import ui.vvm.swingModels.toSingleSelectionModel

class LangSettingsView : OnePixelSplitter(false, 0.3F), View<LangSettingsViewModel> {

    private val sideList: JBList<Lang> = JBList<Lang>()
    private val mainContainer: SingleChildContainer

    init {

        val listContainer = ToolbarDecorator.createDecorator(sideList).createPanel()

        mainContainer = SingleChildContainer("Select a Language", JBLabel("Amazing work"))

        firstComponent = listContainer
        secondComponent = mainContainer

    }


    override fun CoroutineScope.onViewModelBind(viewModel: LangSettingsViewModel) {
        sideList.model = viewModel.languages.toCollectionListModel(this)
        sideList.selectionModel = viewModel.selectedLangIndex.toSingleSelectionModel(this)

        launch {
            viewModel.selectedLangIndex.collect {
                mainContainer.setChildVisible(it != -1)
            }
        }
    }
}