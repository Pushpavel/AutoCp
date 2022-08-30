package com.github.pushpavel.autocp.database

import com.intellij.diff.util.FileEditorBase
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class AutoCpFileEditorProvider : FileEditorProvider, DumbAware {
    override fun accept(project: Project, file: VirtualFile): Boolean {
        return file.name == ".autocp"
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        return object : FileEditorBase() {
            override fun getComponent() = panel {
                indent {
                    row {
                        label("AutoCp Storage File").applyToComponent {
                            font = font.deriveFont(32F)
                        }
                    }
                    row {
                        label("Stores parsed problems and testcases")
                    }
                    row {
                        label(
                            "You should not edit this file. If you did AutoCp plugin would exhibit undesired behaviour"
                        ).bold()
                    }
                }
            }

            override fun getName() = "AutoCp Storage"
            override fun getPreferredFocusedComponent(): JComponent? = null
            override fun getFile() = file
        }
    }

    override fun getEditorTypeId(): String {
        return "Autocp Storage File warning"
    }

    override fun getPolicy() = FileEditorPolicy.HIDE_DEFAULT_EDITOR
}