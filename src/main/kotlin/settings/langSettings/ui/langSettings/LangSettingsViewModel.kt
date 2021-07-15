package settings.langSettings.ui.langSettings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import settings.langSettings.model.Lang
import settings.langSettings.ui.dialogs.IDELangSelectorDialog
import settings.langSettings.ui.langItem.LangItemViewModel
import ui.vvm.ViewModel

class LangSettingsViewModel : ViewModel() {
    val selectedLangIndex = MutableStateFlow(-1)
    val languages = MutableStateFlow<List<Lang>>(emptyList())
    val itemModel = LangItemViewModel(languages, selectedLangIndex)

    fun addNewLanguage() {
        val selectedLanguage = IDELangSelectorDialog().showAndGetSelection() ?: return
        val langList = languages.value.toMutableList()

        langList.add(Lang(selectedLanguage.id, null, emptyList()))
        scope.launch {
            languages.emit(langList)
        }
    }

}