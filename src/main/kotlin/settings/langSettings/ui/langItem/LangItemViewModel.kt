package settings.langSettings.ui.langItem

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import settings.langSettings.model.BuildConfig
import settings.langSettings.model.Lang
import settings.langSettings.ui.dialogs.buildConfigDialog.BuildConfigDialog
import ui.vvm.ViewModel

class LangItemViewModel(
    private val languages: MutableStateFlow<List<Lang>>,
    private val selectedLangIndex: StateFlow<Int>
) : ViewModel() {

    val buildConfig = languages.combine(selectedLangIndex) { list, index ->
        if (index == -1)
            emptyList()
        else
            list[index].buildProperties
    }

    val buildConfigChanges = MutableSharedFlow<List<BuildConfig>>()

    val selectedConfigIndex = MutableStateFlow(-1)

    init {
        scope.launch {
            buildConfigChanges.collect {
                val list = languages.value.toMutableList()
                val index = selectedLangIndex.value
                if (index == -1) return@collect
                list[index] = list[index].copy(buildProperties = it)
                languages.emit(list)
            }
        }
    }


    fun addNewConfig() {
        val langList = languages.value.toMutableList()
        val langIndex = selectedLangIndex.value
        if (langIndex == -1)
            return
        val list = langList[langIndex].buildProperties.toMutableList()
        val index = selectedConfigIndex.value
        val config = BuildConfigDialog(BuildConfig("", ""), list).showAndGetConfig() ?: return

        list.add(index + 1, config)
        langList[langIndex] = langList[langIndex].copy(buildProperties = list)
        scope.launch {
            languages.emit(langList)
            selectedConfigIndex.emit(index + 1)
        }
    }


    fun editConfig() {
        val langList = languages.value.toMutableList()
        val langIndex = selectedLangIndex.value
        if (langIndex == -1)
            return
        val list = langList[langIndex].buildProperties.toMutableList()
        val index = selectedConfigIndex.value
        if (index == -1)
            return
        val config = BuildConfigDialog(list[index], list).showAndGetConfig()

        if (config != null) {
            list[index] = config
            langList[langIndex] = langList[langIndex].copy(buildProperties = list)
            scope.launch {
                languages.emit(langList)
            }
        }

    }
}