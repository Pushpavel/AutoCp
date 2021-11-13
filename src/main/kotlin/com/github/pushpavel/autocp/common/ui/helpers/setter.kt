package com.github.pushpavel.autocp.common.ui.helpers

import javax.swing.JComponent
import kotlin.reflect.KProperty


fun <T> setter(value: T, setFunc: SetterDelegate<T>.(T) -> Unit) = SetterDelegate(value, setFunc)
fun <T> JComponent.layoutUpdater(value: T, setFunc: SetterDelegate<T>.(T) -> Unit) = SetterDelegate(value) {
    setFunc(it)
    revalidate()
}


class SetterDelegate<T>(var value: T, val setFunc: SetterDelegate<T>.(T) -> Unit) {
    init {
        setFunc(value)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        setFunc(value)
        this.value = value
    }
}