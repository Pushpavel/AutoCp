package settings.langSettings.ui.dialogs.buildConfigDialog

import kotlinx.coroutines.flow.MutableStateFlow
import settings.langSettings.model.BuildConfig
import ui.vvm.ViewModel

class BuildConfigViewModel(config: BuildConfig, list: List<BuildConfig>) : ViewModel() {
    val name = MutableStateFlow(config.name)
    val buildCommand = MutableStateFlow(config.buildCommand)
}