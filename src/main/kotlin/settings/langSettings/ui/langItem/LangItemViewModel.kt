package settings.langSettings.ui.langItem

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import settings.langSettings.model.BuildConfig
import settings.langSettings.model.Lang
import ui.vvm.ViewModel

class LangItemViewModel(languages: MutableStateFlow<List<Lang>>, selectedLangIndex: StateFlow<Int>) : ViewModel() {

    val buildProperties = languages.combine(selectedLangIndex) { list, index ->
        if (index == -1)
            emptyList()
        else
            list[index].buildProperties
    }

    val buildPropertiesChanges = MutableSharedFlow<List<BuildConfig>>()

    init {
        scope.launch {
            buildPropertiesChanges.collect {
                println(it)
                val list = languages.value.toMutableList()
                val index = selectedLangIndex.value
                if (index == -1) return@collect
                list[index] = list[index].copy(buildProperties = it)
                languages.emit(list)
            }
        }
    }
}