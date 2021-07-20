package settings.langSettings.ui.langSettings

import com.intellij.lang.Language
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import common.diff.DiffAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import settings.langSettings.model.Lang
import settings.langSettings.ui.langItem.LangItemView
import ui.StringCellRenderer
import ui.layouts.SingleChildContainer
import ui.vvm.View
import ui.vvm.bind
import ui.vvm.swingModels.toCollectionListModel
import ui.vvm.swingModels.toSingleSelectionModel
import javax.swing.BorderFactory

class LangSettingsView : OnePixelSplitter(false, 0.3F), View<LangSettingsViewModel> {

    private val sideList = JBList<Lang>()
    private val mainContainer: SingleChildContainer
    private val langItemView = LangItemView()

    init {

        sideList.cellRenderer = StringCellRenderer<Lang> {
            val lang = Language.findLanguageByID(it.langId)
            if (lang == null) null else
                Pair(lang.displayName, lang.associatedFileType?.icon)
        }


        mainContainer = SingleChildContainer("Select a Language", langItemView)

        secondComponent = mainContainer.apply {
            border = BorderFactory.createEmptyBorder(0, 8, 0, 0)
        }
    }


    override fun CoroutineScope.onViewModelBind(viewModel: LangSettingsViewModel) {

        bind(langItemView, viewModel.itemModel)

        val listContainer = ToolbarDecorator.createDecorator(sideList)
            .setAddAction { viewModel.addNewLanguage() }
            .createPanel()

        firstComponent = listContainer


        sideList.model = viewModel.languages.toCollectionListModel(
            this, viewModel.languages,
            object : DiffAdapter<Lang> {
                override fun isSame(item1: Lang, item2: Lang) = item1.langId == item2.langId
            }
        )

        sideList.selectionModel = viewModel.selectedLangIndex.toSingleSelectionModel(this, viewModel.selectedLangIndex)

        launch {
            viewModel.selectedLangIndex.collect {
                mainContainer.setChildVisible(it != -1)
            }
        }
    }
}