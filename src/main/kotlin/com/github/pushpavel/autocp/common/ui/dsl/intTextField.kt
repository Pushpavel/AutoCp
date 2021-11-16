package com.github.pushpavel.autocp.common.ui.dsl

import com.intellij.ui.UIBundle
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.Cell
import com.intellij.ui.layout.CellBuilder
import com.intellij.ui.layout.PropertyBinding
import com.intellij.util.MathUtil
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

/**
 * intTextField method has an additional parameter @param step in IDE build-204 and above which made this plugin
 * binary incompatible with IDE versions 203. So, that method is now moved to this plugin's source to be backwards
 * compatible.
 * TODO: remove this method if alternative method found or  IDE build-203 is to be unsupported
 *
 * @param step allows changing value by up/down keys on keyboard
 */
@JvmOverloads
fun Cell.intTextFieldCompat(
    binding: PropertyBinding<Int>,
    columns: Int? = null,
    range: IntRange? = null,
    step: Int? = null
): CellBuilder<JBTextField> {
    return textField(
        { binding.get().toString() },
        { value ->
            value.toIntOrNull()
                ?.let { intValue -> binding.set(range?.let { intValue.coerceIn(it.first, it.last) } ?: intValue) }
        },
        columns
    ).withValidationOnInput {
        val value = it.text.toIntOrNull()
        when {
            value == null -> error(UIBundle.message("please.enter.a.number"))
            range != null && value !in range -> error(
                UIBundle.message(
                    "please.enter.a.number.from.0.to.1",
                    range.first,
                    range.last
                )
            )
            else -> null
        }
    }.apply {
        step?.let {
            component.addKeyListener(object : KeyAdapter() {
                override fun keyPressed(e: KeyEvent?) {
                    val increment: Int = when (e?.keyCode) {
                        KeyEvent.VK_UP -> step
                        KeyEvent.VK_DOWN -> -step
                        else -> return
                    }

                    var value = component.text.toIntOrNull()
                    if (value != null) {
                        value += increment
                        if (range != null) {
                            value = MathUtil.clamp(value, range.first, range.last)
                        }
                        component.text = value.toString()
                        e.consume()
                    }
                }
            })
        }
    }
}