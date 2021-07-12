package ui.views

import com.intellij.ui.components.JBPanelWithEmptyText
import javax.swing.JComponent

class ContainerView(emptyText: String, private val child: JComponent) : JBPanelWithEmptyText(), View {
    init {
        withEmptyText(emptyText)
        setChildVisible(true)
    }

    fun setChildVisible(visible: Boolean) {
        if (visible == (componentCount != 0))
            return

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