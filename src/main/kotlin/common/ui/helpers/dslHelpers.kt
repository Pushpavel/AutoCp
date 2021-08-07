package common.ui.helpers

import com.intellij.openapi.ui.ValidationInfo

fun ValidationInfo?.isError(): Boolean {
    return this?.warning == false
}