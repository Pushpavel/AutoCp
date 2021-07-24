package settings.langSettings.ui.langSettings

import com.intellij.lang.Language
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import settings.langSettings.model.Lang
import settings.langSettings.ui.langItem.LangItemView
import ui.StringCellRenderer
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
        sideList.cellRenderer = StringCellRenderer<Lang> {
            val lang = Language.findLanguageByID(it.langId)
            if (lang == null) null else
                Pair(lang.displayName, lang.associatedFileType?.icon)
        }

        sideList.model = collectionListModel(
            viewModel.languages,
            viewModel.languages
        ) { item1, item2 -> item1.langId == item2.langId }

        sideList.selectionModel = singleSelectionModel(viewModel.selectedLangIndex)

        launch {
            viewModel.selectedLangIndex.collect {
                mainContainer.setChildVisible(it != -1)
            }
        }
    }

}