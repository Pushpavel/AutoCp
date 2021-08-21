package common.ui.swing.editableList

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBUI
import java.awt.Component
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.Box
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener


class EditableListView<T>(
    val model: CollectionListModel<T>,
    val itemViewFactory: (T) -> ListItemView<T>,
    val itemFactory: () -> T,
    private val createButtonText: String,
) : JBScrollPane(), ListDataListener, Disposable {

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
            val itemView = itemViewFactory(it)
            itemView.contentChanged(it)

            itemViewMap[itemView.component] = itemView
            listPanel.add(itemView.component, itemConstraints)
        }

        // Create Button
        listPanel.add(panel {
            blockRow { }
            row {
                button(createButtonText) {
                    val item = itemFactory()
                    model.add(item)
                }
            }
        }.apply {
            border = JBUI.Borders.emptyLeft(8)
        }, itemConstraints)

        // fills remaining space
        listPanel.add(Box.createVerticalGlue(), GridBagConstraints().apply { weighty = 1.0 })

        setViewportView(listPanel)

        model.addListDataListener(this)
    }

    override fun intervalAdded(event: ListDataEvent) {
        for (i in event.index0..event.index1) {
            val it = model.items[i]
            val itemView = itemViewFactory(it)
            itemView.contentChanged(it)

            itemViewMap[itemView.component] = itemView
            listPanel.add(itemView.component, itemConstraints, i)
        }
        updateUI()
    }

    override fun intervalRemoved(event: ListDataEvent) {
        for (i in event.index0..event.index1) {
            val it = listPanel.components[i]
            listPanel.remove(it)
            itemViewMap.remove(it)?.let { itemView -> Disposer.dispose(itemView) }
        }
        updateUI()
    }

    override fun contentsChanged(event: ListDataEvent) {
        for (i in event.index0..event.index1) {
            val it = listPanel.components[i]
            val itemView = itemViewMap[it]
            itemView?.contentChanged(model.items[i])
        }
        updateUI()
    }

    override fun dispose() {
        model.removeListDataListener(this)
    }

}