package settings.langSettings.ui.langSettings

import kotlinx.coroutines.flow.MutableStateFlow
import settings.langSettings.model.Lang
import settings.langSettings.ui.ideLangSelector.IDELangSelectorDialog

class LangSettingsViewModel {
    val selectedLangIndex = MutableStateFlow(-1)
    val languages = MutableStateFlow<List<Lang>>(emptyList())


    fun addNewLanguage() {
        val selectedLanguage = IDELangSelectorDialog().showAndGetSelection() ?: return
        val langList = languages.value

        val mutableLangList = langList.toMutableList()
        mutableLangList.add(Lang(selectedLanguage.id, null, emptyList()))

        languages.tryEmit(mutableLangList)
    }

}