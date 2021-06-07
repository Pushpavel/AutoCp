package plugin.settings

import com.intellij.icons.AllIcons
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBFont

class ErrorComponent : JBPanel<ErrorComponent>() {

    private val textComponent = JBLabel()

    init {
        add(JBLabel(AllIcons.General.BalloonError))
        add(JBLabel("Error: ").apply { font = JBFont.label().asBold() })
        add(textComponent)
        isVisible = false
    }


    fun setErrorMessage(message: String?) {
        isVisible = message != null
        textComponent.text = message
    }
}