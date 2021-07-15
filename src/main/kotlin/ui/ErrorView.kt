package ui

import com.intellij.icons.AllIcons
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBFont
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ui.vvm.View

/**
 * Error UI with error icon and a message
 */
class ErrorView : JBPanel<ErrorView>(), View<Flow<String?>> {

    private val textComponent = JBLabel()

    init {
        add(JBLabel(AllIcons.General.BalloonError))
        add(JBLabel("Error: ").apply { font = JBFont.label().asBold() })
        add(textComponent)
        isVisible = false
    }

    override fun CoroutineScope.onViewModelBind(viewModel: Flow<String?>) {
        launch {
            viewModel.collect {
                textComponent.text = it
                isVisible = it != null
            }
        }
    }
}