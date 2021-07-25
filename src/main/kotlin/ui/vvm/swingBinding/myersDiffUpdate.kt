package ui.vvm.swingBinding

import com.intellij.ui.CollectionListModel
import org.assertj.core.util.diff.Delta
import org.assertj.core.util.diff.myers.MyersDiff

fun <T> CollectionListModel<T>.update(list: List<T>) {
    val diff = MyersDiff<T>()
    val deltas = diff.diff(items, list).deltas

    deltas.forEach {
        when (it.type!!) {
            Delta.TYPE.CHANGE -> {
                it.revised.lines.forEachIndexed { index, item ->
                    if (item != null)
                        setElementAt(item, it.revised.position + index)
                }
            }
            Delta.TYPE.DELETE -> {
                removeRange(it.revised.position, it.revised.size())
            }
            Delta.TYPE.INSERT -> {
                addAll(it.revised.position, it.revised.lines)
            }
        }
    }
}