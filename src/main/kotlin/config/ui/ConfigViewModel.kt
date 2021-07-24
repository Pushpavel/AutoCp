package config.ui

import com.intellij.openapi.vfs.VirtualFileManager
import common.isNotIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import settings.langSettings.AutoCpLangSettings
import settings.langSettings.model.BuildConfig
import ui.vvm.ViewModel
import kotlin.io.path.Path

class ConfigViewModel(
    parentScope: CoroutineScope?,
    solutionPath: String,
    buildConfigId: Long?,
) : ViewModel(parentScope) {
    val solutionFilePath = MutableStateFlow(solutionPath)

    val errorMessage = MutableStateFlow<String?>(null)

    val buildConfigs = solutionFilePath.map {
        if (it.isEmpty()) {
            errorMessage.value = "Solution Path is Required"
            return@map listOf()
        }

        val file = VirtualFileManager.getInstance().findFileByNioPath(Path(it))

        if (file == null) {
            errorMessage.value = "Solution Path does not contain a valid solution file"
            return@map listOf()
        }

        val lang = AutoCpLangSettings.findLangByFile(file)

        if (lang == null) {
            errorMessage.value = "solution File is not associated with any language in AutoCp settings"
            return@map listOf()
        }

        errorMessage.value = null
        lang.buildConfigs
    }
    val selectedBuildConfigIndex = MutableStateFlow(-1)


    init {
        scope.launch {
            val index = getIndexOfBuildConfigId(buildConfigId)
            selectedBuildConfigIndex.emit(index)
        }

    }

    suspend fun getIndexOfBuildConfigId(id: Long?): Int {
        if (id == null) return -1
        val configs = buildConfigs.take(1).single()
        return configs.indexOfFirst { it.id == id }
    }

    suspend fun getBuildConfigOfIndex(index: Int): BuildConfig? {
        if (index < 0) return null
        val configs = buildConfigs.take(1).single()
        if (configs.isNotIndex(index)) return null
        return configs[index]
    }
}