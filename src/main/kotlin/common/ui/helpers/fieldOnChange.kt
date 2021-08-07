package common.ui.helpers

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> afterChange(initialValue: T, action: (T) -> Unit): ReadWriteProperty<Any?, T> {
    return object : ReadWriteProperty<Any?, T> {
        var value = initialValue

        override fun getValue(thisRef: Any?, property: KProperty<*>): T = value

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            if (this.value != value) {
                this.value = value
                action(value)
            }
        }
    }
}