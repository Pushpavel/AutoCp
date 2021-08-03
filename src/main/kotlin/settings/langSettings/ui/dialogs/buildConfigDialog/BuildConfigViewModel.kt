@file:Suppress("DuplicatedCode")

package settings.langSettings.ui.dialogs.buildConfigDialog

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import settings.generalSettings.AutoCpGeneralSettings
import settings.langSettings.model.BuildConfig
import ui.vvm.ViewModel

class BuildConfigViewModel(
    parentScope: CoroutineScope?,
    config: BuildConfig,
    list: List<BuildConfig>,
) : ViewModel(parentScope) {
    val name = MutableStateFlow(config.name)
    val buildCommand = MutableStateFlow(config.buildCommand)


    val nameErrors = name.map { name ->
        when {
            list.count { it.name == name } > 1 -> "$name already exists"
            name.isBlank() -> "name should not be empty"
            else -> null
        }
    }

    val buildCommandErrors = buildCommand.map { buildCommand ->
        val input = buildCommand.contains(AutoCpGeneralSettings.INPUT_PATH_KEY)
        val output = buildCommand.contains(AutoCpGeneralSettings.OUTPUT_PATH_KEY)

        // TODO: check if single quotes are useful so that we can just hardcode double quotes directly on the path
        val inputDoubleQuotes = buildCommand.contains("\"" + AutoCpGeneralSettings.INPUT_PATH_KEY + "\"")
        val inputSingleQuotes = buildCommand.contains("'" + AutoCpGeneralSettings.INPUT_PATH_KEY + "'")
        val outputDoubleQuotes = buildCommand.contains("\"" + AutoCpGeneralSettings.OUTPUT_PATH_KEY + "\"")
        val outputSingleQuotes = buildCommand.contains("'" + AutoCpGeneralSettings.OUTPUT_PATH_KEY + "'")

        when {
            !input -> "buildCommand: ${AutoCpGeneralSettings.INPUT_PATH_KEY} missing"
            !output -> "buildCommand: ${AutoCpGeneralSettings.OUTPUT_PATH_KEY} missing"
            !inputSingleQuotes && !inputDoubleQuotes -> "buildCommand: ${AutoCpGeneralSettings.INPUT_PATH_KEY} should be wrapped with single or double quotes"
            !outputSingleQuotes && !outputDoubleQuotes -> "buildCommand: ${AutoCpGeneralSettings.INPUT_PATH_KEY} should be wrapped with single or double quotes"
            else -> null
        }
    }

    val isValid = nameErrors.combine(buildCommandErrors) { e1, e2 -> (e1 ?: e2) == null }

}