package common.ui.poplist

import com.intellij.ui.CollectionListModel
import com.intellij.ui.SingleSelectionModel
import kotlin.math.max

/**
 * Base class for ViewModel of [PopList]
 */
abstract class PopListModel<T> {
    val listModel = CollectionListModel<T>()
    val selectionModel = SingleSelectionModel()

    private val selectionListeners = ArrayList<SelectionListener>()

    // helper for subscribing and to set selection index

    private var lastSelectedIndex = selectionModel.minSelectionIndex

    init {
        // notify selection Listeners
        selectionModel.addListSelectionListener {
            if (lastSelectedIndex != selectionModel.minSelectionIndex) {
                selectionListeners.forEach { listener ->
                    listener.onSelect(selectionModel.minSelectionIndex)
                }
            }
            lastSelectedIndex = selectionModel.minSelectionIndex
        }
    }

    fun setSelectionIndex(index: Int) {
        selectionModel.setSelectionInterval(index, index)
    }

    fun addSelectionListener(selectionListener: SelectionListener) {
        selectionListeners.add(selectionListener)
    }

    fun removeSelectionListener(selectionListener: SelectionListener) {
        selectionListeners.remove(selectionListener)
    }

    protected abstract val itemNameRegex: Regex

    protected abstract fun getItemName(item: T): String

    protected abstract fun buildItemName(name: String, suffix: String): String

    protected abstract fun createNewItem(from: T?): T

    protected fun nextUniqueName(preferredName: String): String {
        if (!listModel.items.map { getItemName(it) }.contains(preferredName))
            return preferredName
        val prefix = itemNameRegex.matchEntire(preferredName)?.groupValues?.getOrNull(1) ?: preferredName

        // find appropriate number for suffixing
        var suffixMaxNumber = 0
        listModel.items.forEach {
            val matches = itemNameRegex.matchEntire(getItemName(it))?.groupValues
            if (matches != null && matches[1] == prefix)
                suffixMaxNumber = max(suffixMaxNumber, matches[2].toInt())
        }

        suffixMaxNumber++

        return buildItemName(prefix, suffixMaxNumber.toString())
    }


    fun addNewOrDuplicateItem() {
        if (lastSelectedIndex == -1) {
            listModel.add(createNewItem(null))
            setSelectionIndex(listModel.size - 1)
        } else {
            val selectedItem = listModel.items[lastSelectedIndex]
            listModel.add(lastSelectedIndex + 1, createNewItem(selectedItem))
            setSelectionIndex(lastSelectedIndex + 1)
        }
    }

    fun interface SelectionListener {
        fun onSelect(index: Int)
    }
}