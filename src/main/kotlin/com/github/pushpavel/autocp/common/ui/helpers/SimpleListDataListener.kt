package com.github.pushpavel.autocp.common.ui.helpers

import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener


fun interface SimpleListDataListener : ListDataListener {

    fun onChange()

    override fun intervalAdded(p0: ListDataEvent?) = onChange()

    override fun intervalRemoved(p0: ListDataEvent?) = onChange()

    override fun contentsChanged(p0: ListDataEvent?) = onChange()
}