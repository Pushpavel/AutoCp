package config.ui

import com.intellij.openapi.vfs.VirtualFileManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import settings.langSettings.AutoCpLangSettings
import kotlin.io.path.Path

class ConfigViewModel(solutionPath: String, buildConfigId: Long?) {
    val solutionFilePath = MutableStateFlow(solutionPath)

    val buildConfigs = solutionFilePath.map {
        val file = VirtualFileManager.getInstance().findFileByNioPath(Path(it))
        AutoCpLangSettings.findLangByFile(file)?.buildConfigs ?: listOf()
    }

    val selectedBuildConfigId = MutableStateFlow(buildConfigId)
}