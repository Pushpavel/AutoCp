package settings.langSettings.ui.langItem

import com.intellij.openapi.Disposable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import settings.langSettings.model.BuildProperties
import settings.langSettings.model.Lang
import ui.vvm.ViewModel

class LangItemViewModel(languages: MutableStateFlow<List<Lang>>, selectedLangIndex: StateFlow<Int>) : ViewModel() {

    val buildProperties = MutableStateFlow<List<BuildProperties>>(emptyList())

    init {
        // parent to item
        scope.launch {
            languages.combine(selectedLangIndex) { list, index ->
                if (index == -1)
                    emptyList()
                else
                    list[index].buildProperties
            }.collect {
                buildProperties.emit(it)
            }
        }

        // item to parent
        scope.launch {
            buildProperties
                .collect {
                    val list = languages.value.toMutableList()
                    val index = selectedLangIndex.value
                    if (index == -1) return@collect
                    list[index] = list[index].copy(buildProperties = it)
                    languages.emit(list)
                }
        }
    }
}