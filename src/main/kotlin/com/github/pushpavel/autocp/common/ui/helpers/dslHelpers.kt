package com.github.pushpavel.autocp.common.ui.helpers

import com.intellij.openapi.ui.ValidationInfo

fun ValidationInfo?.isError(): Boolean {
    return this?.warning == false
}
//
//fun CellBuilder<JBCheckBox>.isSelected(): ComponentPredicate {
//    return object : ComponentPredicate() {
//
//        override fun addListener(listener: (Boolean) -> Unit) {
//            component.addActionListener {
//                listener(component.isSelected)
//            }
//        }
//
//        override fun invoke() = component.isSelected
//
//    }
//}