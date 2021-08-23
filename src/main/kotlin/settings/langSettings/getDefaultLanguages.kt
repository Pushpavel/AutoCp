package settings.langSettings

import com.intellij.execution.Platform
import com.intellij.lang.Language
import com.intellij.util.io.readText
import common.res.R
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import settings.langSettings.model.Lang
import java.nio.file.Path

fun getDefaultLanguages(): Map<String, Lang> {
    return R.files.langJsons
        .map<Path, Lang> { Json.decodeFromString(it.readText()) }
        // filtering languages supported by the IDE
        .filter { Language.findLanguageByID(it.langId) != null }
        .map { lang ->
            val buildConfigs = lang.buildConfigs.mapValues {
                it.value.copy(
                    buildCommand = processCommand(it.value.buildCommand),
                    executeCommand = processCommand(it.value.executeCommand)
                )
            }

            lang.copy(buildConfigs = buildConfigs)
        }
        // converting to map
        .associateBy { it.langId }
}

private fun processCommand(command: String): String {
    var c = command
    for (k in getPlatformMacros())
        c = c.replace(k.key, k.value)
    return platformPaths(c)
}


private fun platformPaths(command: String): String {
    return when (Platform.current()) {
        Platform.WINDOWS -> command.replace('/', '\\')
        Platform.UNIX -> command.replace('\\', '/')
    }
}

private fun getPlatformMacros(): Map<String, String> {
    return when (Platform.current()) {
        Platform.WINDOWS -> mapOf(
            R.keys.execExtensionMacro to ".exe"
        )
        Platform.UNIX -> mapOf(
            R.keys.execExtensionMacro to ""
        )
    }
}