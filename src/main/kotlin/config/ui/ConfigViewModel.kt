package config.ui

import com.intellij.openapi.vfs.VirtualFileManager
import common.isNotIndex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import settings.langSettings.AutoCpLangSettings
import settings.langSettings.model.BuildConfig
import ui.vvm.ViewModel
import kotlin.io.path.Path

class ConfigViewModel(solutionPath: String, buildConfigId: Long?) : ViewModel() {
    val solutionFilePath = MutableStateFlow(solutionPath)

    val buildConfigs = solutionFilePath.map {
        val file = VirtualFileManager.getInstance().findFileByNioPath(Path(it))
        AutoCpLangSettings.findLangByFile(file)?.buildConfigs ?: listOf()
    }

    val selectedBuildConfigId = MutableStateFlow(buildConfigId)
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