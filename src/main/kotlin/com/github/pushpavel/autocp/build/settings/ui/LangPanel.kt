package com.github.pushpavel.autocp.build.settings.ui

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.common.ui.dsl.DslCallbacks
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.runUndoTransparentWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
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

    private val buildCommandEditor = editorFactory.createEditor(buildCommandDoc).apply { customizeEditor() }
    private val executeCommandEditor = editorFactory.createEditor(executeCommandDoc).apply { customizeEditor() }
    var lineCommentPrefix = ""

    val component: DialogPanel = panel {
        row("Single line comment prefix") {
            textField(::lineCommentPrefix, 2).apply {
                onReset { component.isEnabled = lang?.isDefault == false }
            }
        }
        row("Build Command") { buildCommandEditor.component(CCFlags.growX) }
        row("Execute Command") { executeCommandEditor.component(CCFlags.growX) }
    }.withBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4))


    private fun Editor.customizeEditor() {
        settings.apply {
            isUseSoftWraps = true
            additionalLinesCount = 0
            isLineMarkerAreaShown = false
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
            lineCommentPrefix = l.lineCommentPrefix
            component.reset()
        }
    }

    override fun isModified(): Boolean {
        return lang != null
                && (lang!!.buildCommand != buildCommandDoc.text.takeIf { it.isNotBlank() }
                || lang!!.executeCommand != executeCommandDoc.text
                || lang!!.lineCommentPrefix != lineCommentPrefix
                )
    }

    override fun apply() {
        lang = lang?.copy(
            buildCommand = buildCommandDoc.text.takeIf { it.isNotBlank() },
            executeCommand = executeCommandDoc.text,
            lineCommentPrefix = lineCommentPrefix
        )
    }


    override fun dispose() {
        editorFactory.releaseEditor(buildCommandEditor)
        editorFactory.releaseEditor(executeCommandEditor)
    }
}