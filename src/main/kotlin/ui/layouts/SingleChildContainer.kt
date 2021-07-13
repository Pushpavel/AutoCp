package ui.layouts

import com.intellij.ui.components.JBPanelWithEmptyText
import javax.swing.JComponent

/**
 *
 */
class SingleChildContainer(emptyText: String, private val child: JComponent) : JBPanelWithEmptyText() {

    init {
        withEmptyText(emptyText)
    }

    fun setChildVisible(visible: Boolean) {
        if (visible == (componentCount != 0)) return

        if (visible) attachItemView() else detachItemView()
    }

    private fun attachItemView() {
        add(child)
        updateUI()
    }

    private fun detachItemView() {
        remove(child)
        updateUI()
    }
}