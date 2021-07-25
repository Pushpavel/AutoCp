package settings.generalSettings.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import settings.langSettings.model.Lang
import ui.vvm.ViewModel

class GeneralSettingsViewModel(parentScope: CoroutineScope?) : ViewModel(parentScope) {
    val langList = MutableStateFlow<List<Lang>>(listOf())
    val selectedLangIndex = MutableStateFlow(-1)
}