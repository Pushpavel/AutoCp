package ui

import com.intellij.diff.util.FileEditorBase
import com.intellij.ui.layout.panel
import javax.swing.JComponent

class AutoCpFileEditor : FileEditorBase() {
    companion object {
        private const val NAME = "AutoCP Problem"
    }


    override fun getName() = NAME

    override fun getPreferredFocusedComponent() = component

    override fun getComponent(): JComponent {
        return panel{
            row {
                label("Super working")
            }
        }
    }
}