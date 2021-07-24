package ui

import com.intellij.icons.AllIcons
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBFont
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ui.helpers.viewScope

/**
 * Error UI with error icon and a message
 */
class ErrorView(parentScope: CoroutineScope, errorMessage: Flow<String?>) : JBPanel<ErrorView>() {
    val scope = viewScope(parentScope)
    private val textComponent = JBLabel()

    init {
        add(JBLabel(AllIcons.General.BalloonError))
        add(JBLabel("Error: ").apply { font = JBFont.label().asBold() })
        add(textComponent)
        isVisible = false

        scope.launch {
            errorMessage.collect {
                textComponent.text = it
                isVisible = it != null
            }
        }
    }
}