package settings.langSettings.ui.langSettings

import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import settings.langSettings.model.Lang
import settings.langSettings.ui.langItem.LangItemView
import ui.helpers.viewScope
import ui.layouts.SingleChildContainer
import ui.vvm.swingModels.collectionListModel
import ui.vvm.swingModels.singleSelectionModel
import javax.swing.BorderFactory

class LangSettingsView(val viewModel: LangSettingsViewModel) : OnePixelSplitter(false, 0.3F) {

    val scope = viewScope(viewModel.scope)

    private val sideList = JBList<Lang>()
    private val mainContainer: SingleChildContainer
    private val langItemView = LangItemView(viewModel.itemModel)

    init {

        mainContainer = SingleChildContainer("Select a Language", langItemView)

        firstComponent = ToolbarDecorator.createDecorator(sideList)
            .setAddAction { viewModel.addNewLanguage() }
            .createPanel()

        secondComponent = mainContainer.apply {
            border = BorderFactory.createEmptyBorder(0, 8, 0, 0)
        }

        scope.launch { bind() }
    }

    private fun CoroutineScope.bind() {
        sideList.cellRenderer = Lang.cellRenderer()

        sideList.model = collectionListModel(
            viewModel.languages,
            viewModel.languages
        )

        sideList.selectionModel = singleSelectionModel(viewModel.selectedLangIndex)

        launch {
            viewModel.selectedLangIndex.collect {
                mainContainer.setChildVisible(it != -1)
            }
        }
    }

}