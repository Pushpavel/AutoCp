package com.github.pushpavel.autocp.build

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