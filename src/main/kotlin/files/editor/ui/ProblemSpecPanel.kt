package files.editor.ui

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.layout.panel
import files.ProblemSpec
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent


class ProblemSpecPanel(
    state: ProblemSpec?, onComponentUpdate: ((ProblemSpec?) -> Unit)?
) : UIComponent<DialogPanel, ProblemSpec?>(state, onComponentUpdate) {

    private lateinit var problemNameLabel: JBTextArea

    override val component = panel {
        row {
            mainComponent(state)()
        }
    }

    private fun mainComponent(state: ProblemSpec?) = state?.let {
        actualComponent(it)
    } ?: panel {
        row {
            label("Some Internal Error (AutoCp)")
        }
    }

    private fun actualComponent(state: ProblemSpec) = panel {
        row {
            textArea(state::name, {}, null, null).withLeftGap().component.also {
                problemNameLabel = it
                it.onChange {
                    state.name = problemNameLabel.text
                    setState()
                }
            }
        }
    }

    override fun onStateUpdate(state: ProblemSpec?) {
        val prevState = this.state

        if (state == null || prevState == null) {
            component.removeAll()
            component.add(mainComponent(state))
            markDirty()
            return
        }

        if (state.name != prevState.name) {
            problemNameLabel.text = state.name
            markDirty()
        }
    }
}