package com.github.pushpavel.autocp.build

import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.common.ui.swing.TileCellRenderer
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.fileTypes.LanguageFileType

data class Lang(
    val extension: String,
    val buildCommand: String?,
    val executeCommand: String,
    val lineCommentPrefix: String,
    val isDefault: Boolean,
) {


    fun constructBuildCommand(inputPath: String, dirPath: String): String {
        if (buildCommand == null) return "internal error"
        return constructCommand(buildCommand, inputPath, dirPath)
    }

    fun constructExecuteCommand(inputPath: String, dirPath: String): String {
        return constructCommand(executeCommand, inputPath, dirPath)
    }

    private fun constructCommand(command: String, inputPath: String, dirPath: String): String {
        return command.replace(R.keys.inputPathMacro, "\"$inputPath\"")
            .replace(R.keys.dirUnquotedPathMacro, dirPath)
            .replace(R.keys.dirPathMacro, "\"$dirPath\"")
    }

    companion object {
        fun cellRenderer(emptyText: String = "None"): TileCellRenderer<Lang> {
            return TileCellRenderer(emptyText = emptyText) {
                FileTypeManager.getInstance().getFileTypeByExtension(it.extension).let { ft ->
                    text = if (ft is LanguageFileType)
                        ft.language.displayName + " (${it.extension})"
                    else
                        ft.displayName + " (${it.extension})"
                    icon = ft.icon
                }
            }
        }
    }
}

data class DefaultLangData(
    val extension: String,
    val commands: List<Pair<String?, String>>,
    val lineCommentPrefix: String
)