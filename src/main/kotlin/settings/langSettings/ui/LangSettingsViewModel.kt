package settings.langSettings.ui

import kotlinx.coroutines.flow.MutableStateFlow
import settings.langSettings.model.Lang

class LangSettingsViewModel {
    val selectedLangIndex = MutableStateFlow(-1)
    val languages = MutableStateFlow<List<Lang>>(emptyList())


}