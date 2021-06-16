package ui

import com.intellij.icons.AllIcons
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBFont
import kotlin.properties.Delegates

class ErrorComponent(model: Model) : JBPanel<ErrorComponent>() {

    private val textComponent = JBLabel()

    init {
        add(JBLabel(AllIcons.General.BalloonError))
        add(JBLabel("Error: ").apply { font = JBFont.label().asBold() })
        add(textComponent)
        isVisible = false
        model.errorMessageListeners.add { message ->
            textComponent.text = message
            isVisible = message != null
        }
    }

    class Model {
        var errorMessageListeners = ArrayList<(String?) -> Unit>()
        var errorMessage: String? by Delegates.observable(null) { _, oldValue, newValue ->
            if (oldValue != newValue)
                errorMessageListeners.forEach { it(newValue) }
        }
    }
}