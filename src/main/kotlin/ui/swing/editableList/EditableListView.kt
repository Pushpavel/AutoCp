package ui.swing.editableList

import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import java.awt.Component
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.Box
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener


class EditableListView<T>(
    val model: CollectionListModel<T>, val itemFactory: (T) -> ListItemView<T>
) : JBScrollPane(), ListDataListener {

    private val listPanel = JBPanelWithEmptyText(GridBagLayout())
    private val itemViewMap = mutableMapOf<Component, ListItemView<T>>()

    private val itemConstraints = GridBagConstraints().apply {
        gridwidth = GridBagConstraints.REMAINDER
        anchor = GridBagConstraints.FIRST_LINE_START
        fill = GridBagConstraints.HORIZONTAL
        weightx = 1.0
    }

    init {
        model.items.forEach {
            val itemView = itemFactory(it)
            itemViewMap[itemView.component] = itemView
            listPanel.add(itemView.component, itemConstraints)
        }

        // fills remaining space
        listPanel.add(Box.createVerticalGlue(), GridBagConstraints().apply { weighty = 1.0 })

        setViewportView(listPanel)

        model.addListDataListener(this)
    }

    override fun intervalAdded(event: ListDataEvent) {
        for (i in event.index0..event.index1) {
            val it = model.items[i]
            val itemView = itemFactory(it)
            itemViewMap[itemView.component] = itemView
            listPanel.add(itemView.component, itemConstraints)
        }
    }

    override fun intervalRemoved(event: ListDataEvent) {
        for (i in event.index0..event.index1) {
            val it = listPanel.components[i]
            val itemView = itemViewMap[it]
            itemView?.dispose()
            listPanel.remove(it)
        }
    }

    override fun contentsChanged(event: ListDataEvent) {
        for (i in event.index0..event.index1) {
            val it = listPanel.components[i]
            val itemView = itemViewMap[it]
            itemView?.contentChanged(model.items[i])
        }
    }

}