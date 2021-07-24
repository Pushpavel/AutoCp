package settings.langSettings.ui.langItem

import com.intellij.openapi.Disposable
import common.isNotIndex
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import settings.langSettings.model.BuildConfig
import settings.langSettings.model.Lang
import settings.langSettings.ui.dialogs.buildConfigDialog.BuildConfigDialog
import ui.vvm.ViewModel

class LangItemViewModel(
    parentDisposable: Disposable,
    private val languages: MutableStateFlow<List<Lang>>,
    private val selectedLangIndex: StateFlow<Int>,
) : ViewModel(parentDisposable) {

    val buildConfig = languages.combine(selectedLangIndex) { list, index ->
        if (list.isNotIndex(index))
            emptyList()
        else
            list[index].buildConfigs
    }

    val buildConfigChanges = MutableSharedFlow<List<BuildConfig>>()

    val selectedConfigIndex = MutableStateFlow(-1)

    init {
        scope.launch {
            buildConfigChanges.collect {
                val list = languages.value.toMutableList()
                val index = selectedLangIndex.value
                if (list.isNotIndex(index)) return@collect
                list[index] = list[index].copy(buildConfigs = it)
                languages.emit(list)
            }
        }
    }


    fun addNewConfig() {
        val langList = languages.value.toMutableList()
        val langIndex = selectedLangIndex.value
        if (langList.isNotIndex(langIndex))
            return
        val list = langList[langIndex].buildConfigs.toMutableList()
        val index = selectedConfigIndex.value
        val newBlankConfig = BuildConfig(System.currentTimeMillis(), "", "")
        val config = BuildConfigDialog(newBlankConfig, list).showAndGetConfig() ?: return

        list.add(index + 1, config)
        langList[langIndex] = langList[langIndex].copy(buildConfigs = list)
        scope.launch {
            languages.emit(langList)
            selectedConfigIndex.emit(index + 1)
        }
    }


    fun editConfig() {
        val langList = languages.value.toMutableList()
        val langIndex = selectedLangIndex.value
        if (langList.isNotIndex(langIndex))
            return
        val list = langList[langIndex].buildConfigs.toMutableList()
        val index = selectedConfigIndex.value
        if (list.isNotIndex(index))
            return
        val config = BuildConfigDialog(list[index], list).showAndGetConfig() ?: return

        list[index] = config
        langList[langIndex] = langList[langIndex].copy(buildConfigs = list)
        scope.launch {
            languages.emit(langList)
        }

    }
}