package com.github.pushpavel.autocp.common.ui.dsl

import com.intellij.openapi.Disposable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.layout.CellBuilder
import com.intellij.ui.layout.ValidationInfoBuilder
import com.github.pushpavel.autocp.common.ui.helpers.SimpleListDataListener
import javax.swing.JComboBox
import javax.swing.JComponent

fun DialogPanel.startValidating(
    parentDisposable: Disposable,
    componentValidityChangedCallback: ((Map<JComponent, ValidationInfo>) -> Unit)? = null
) {
    val map = customValidationRequestors.toMutableMap()
    for ((component) in componentValidateCallbacks) {
        if (map[component] != null) continue

        if (component is JComboBox<*>) {

            map[component] = mutableListOf({ validate ->
                component.addActionListener { validate() }
                component.model.addListDataListener(SimpleListDataListener { validate() })
            })

        }
    }

    customValidationRequestors = map

    registerValidators(parentDisposable, componentValidityChangedCallback)
}

fun <T : JComponent> CellBuilder<T>.withValidation(callback: ValidationInfoBuilder.(T) -> ValidationInfo?): CellBuilder<T> {
    return withValidationOnInput(callback).withValidationOnApply(callback)
}