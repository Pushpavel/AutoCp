package common.ui.swing.editableList

import com.intellij.openapi.Disposable
import javax.swing.JComponent

interface ListItemView<T> : Disposable {
    val component: JComponent

    fun contentChanged(item: T)
}