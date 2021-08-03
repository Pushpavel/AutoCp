package ui.dsl

import com.intellij.ui.layout.CellBuilder
import javax.swing.JComponent

interface DslCallbacks {
    fun apply() {}
    fun reset() {}
    fun isModified(): Boolean
}

fun <E> CellBuilder<E>.registerDslCallbacks() where  E : JComponent, E : DslCallbacks {
    onApply(component::apply)
    onReset(component::reset)
    onIsModified(component::isModified)
}