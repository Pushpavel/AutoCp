package files.editor.ui

import java.awt.Component
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JComponent

abstract class UIComponent<C : JComponent, T>(protected var state: T, private val onComponentUpdate: ((T) -> Unit)?) {

    abstract val component: C
    private var isDirty = false

    abstract fun onStateUpdate(state: T)

    protected fun setState() = onComponentUpdate?.let { it(state) }

    fun receiveState(state: T) {
        onStateUpdate(state)
        if (!isDirty) return
        this.state = state
        component.revalidate()
        component.repaint()
    }

    protected fun markDirty() {
        isDirty = true
    }

    fun Component.onChange(action: () -> Unit) {
        this.addKeyListener(object : KeyAdapter() {
            override fun keyReleased(e: KeyEvent) = action()
        })
    }

}