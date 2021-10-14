package com.github.pushpavel.autocp.settings.generalSettings

import com.github.pushpavel.autocp.common.res.R
import com.intellij.ui.layout.Row
import com.intellij.ui.layout.RowBuilder
import java.nio.file.InvalidPathException
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.pathString

class FileGenerationRootRow {
    val generalSettings = AutoCpGeneralSettings.instance
    var rootDir = generalSettings.fileGenerationRoot

    fun placeUI(l: RowBuilder): Row {
        return l.row("File Generation Root") {
            textField(::rootDir)
                .onReset { rootDir = generalSettings.fileGenerationRoot }
                .onIsModified { rootDir != generalSettings.fileGenerationRoot }
                .onApply {
                    try {
                        generalSettings.fileGenerationRoot = if (rootDir.isNotBlank())
                            Path(rootDir).pathString
                        else ""
                    } catch (e: InvalidPathException) {
                        // ignored
                    }
                }.withValidationOnInput {
                    try {
                        val p = Path(it.text)
                        when {
                            p.isAbsolute -> error("Should not be an absolute path")
                            p.extension != "" -> error("Should correspond to a directory")
                            else -> null
                        }
                    } catch (e: InvalidPathException) {
                        error(e.localizedMessage)
                    }
                }.comment(R.strings.fileGenerationRootComment)
        }
    }
}