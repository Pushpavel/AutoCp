package com.github.pushpavel.autocp.common.ui.dsl

import com.intellij.ui.dsl.builder.Cell
import javax.swing.JComponent

interface DslCallbacks {
    fun apply() {}
    fun reset() {}
    fun isModified(): Boolean
}

fun <E> Cell<E>.registerDslCallbacks() where  E : JComponent, E : DslCallbacks {
    onApply(component::apply)
    onReset(component::reset)
    onIsModified(component::isModified)
}