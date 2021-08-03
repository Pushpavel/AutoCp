package settings.langSettings.ui.langSettings

import com.intellij.ui.CollectionListModel
import common.isIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import settings.langSettings.model.Lang
import settings.langSettings.ui.dialogs.IDELangSelectorDialog
import settings.langSettings.ui.langItem.LangItemViewModel
import ui.vvm.ViewModel

class LangSettingsViewModel(parentScope: CoroutineScope?) : ViewModel(parentScope) {


    val langListModel = CollectionListModel<Lang>()
    val selectedLangIndex = MutableStateFlow(-1)
    val languages = MutableStateFlow<List<Lang>>(emptyList())
    private val selectedLang = MutableStateFlow<Lang?>(null)
    val itemModel = LangItemViewModel(scope, selectedLang)

    init {
        scope.launch {
            languages.combine(selectedLangIndex) { list, index ->
                if (list.isIndex(index))
                    list[index]
                else
                    null
            }.collect { selectedLang.emit(it) }
        }

        scope.launch {
            selectedLang.collect {
                if (it != null) {
                    val list = languages.value.toMutableList()
                    val index = selectedLangIndex.value
                    if (list.isIndex(index)) {
                        list[index] = it
                        languages.emit(list)
                    }
                }
            }
        }
    }


    fun addNewLanguage() {
        val langList = languages.value.toMutableList()
        val selectedLanguage = IDELangSelectorDialog(langList).showAndGetSelection() ?: return

        langList.add(selectedLangIndex.value + 1, Lang(selectedLanguage.id, null, null, emptyList()))
        scope.launch {
            languages.emit(langList)
            selectedLangIndex.emit(selectedLangIndex.value + 1)
        }
    }

}