package com.github.pushpavel.autocp.common.helpers

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import kotlin.reflect.KProperty

/**
 * Property Delegate that replaces a disposable after disposal.
 * Also calls [onDispose] on disposal.
 */
class DisposableScope(private val onDispose: () -> Unit = { }) {
    private var disposable = Disposable(::replaceOnDispose)

    private fun replaceOnDispose() {
        onDispose()
        disposable = Disposable(::replaceOnDispose)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = disposable
}

/**
 * Helper function to dispose a [Disposable] with [Disposer].
 */
fun Disposable.doDisposal() {
    Disposer.dispose(this)
}