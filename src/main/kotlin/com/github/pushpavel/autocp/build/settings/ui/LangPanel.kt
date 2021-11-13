package com.github.pushpavel.autocp.build.settings.ui

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.common.ui.dsl.DslCallbacks
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.runUndoTransparentWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.CollectionListModel
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import javax.swing.BorderFactory

class LangPanel(val model: CollectionListModel<Lang>) : DslCallbacks, Disposable {
    var lang: Lang? = null
    private val editorFactory = EditorFactory.getInstance()

    private val buildCommandDoc = editorFactory.createDocument("")
    private val executeCommandDoc = editorFactory.createDocument("")

    private val buildCommandEditor =
        editorFactory.createEditor(buildCommandDoc).apply { customizeEditor() }
    private val executeCommandEditor =
        editorFactory.createEditor(executeCommandDoc).apply { customizeEditor() }

    val component: DialogPanel = panel {
        row("Build Command") {
            buildCommandEditor
                .component(CCFlags.growX)
                .comment(R.strings.buildCommandComment)
        }

        row("Execute Command") {
            executeCommandEditor
                .component(CCFlags.growX)
                .comment(R.strings.executeCommandComment)
        }
        commentRow(R.strings.commandTemplateDesc)
    }.withBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4))


    private fun Editor.customizeEditor() {
        if (this is EditorEx)
            isOneLineMode = true

        settings.apply {
            isUseSoftWraps = true
            additionalLinesCount = 0
            isLineMarkerAreaShown = false
            isLineNumbersShown = false
            setUseTabCharacter(false)
        }
    }

    override fun reset() {
        lang?.let { l ->
            runUndoTransparentWriteAction {
                if (l.buildCommand != buildCommandDoc.text)
                    buildCommandDoc.setText(l.buildCommand ?: "")
                if (l.executeCommand != executeCommandDoc.text)
                    executeCommandDoc.setText(l.executeCommand)
            }
            component.reset()
        }
    }

    override fun isModified(): Boolean {
        return lang != null
                && (lang!!.buildCommand != buildCommandDoc.text.takeIf { it.isNotBlank() }?.trim()
                || lang!!.executeCommand != executeCommandDoc.text.trim()
                )
    }

    override fun apply() {
        lang = lang?.copy(
            buildCommand = buildCommandDoc.text.takeIf { it.isNotBlank() }?.trim(),
            executeCommand = executeCommandDoc.text.trim(),
        )
    }


    override fun dispose() {
        editorFactory.releaseEditor(buildCommandEditor)
        editorFactory.releaseEditor(executeCommandEditor)
    }
}
