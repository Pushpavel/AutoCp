package com.github.pushpavel.autocp.testcasetool.components

import com.github.pushpavel.autocp.common.ui.helpers.setter
import com.github.pushpavel.autocp.common.ui.swing.AutoLayout
import com.github.pushpavel.autocp.core.persistance.testcases.Testcase
import com.intellij.openapi.Disposable
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBPanel
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class TestcaseListPanel : JBPanel<TestcaseListPanel>(AutoLayout(autoFitMainLines = true)), ListDataListener,
    Disposable {
    var model: CollectionListModel<Testcase>? by setter(null) {
        model?.removeListDataListener(this@TestcaseListPanel)
        model = it
        model?.addListDataListener(this@TestcaseListPanel)
    }

    override fun intervalAdded(e: ListDataEvent) {
        for (i in e.index0..e.index1)
            add(TestcaseContent())
        updateUI()
    }

    override fun intervalRemoved(e: ListDataEvent) {
        for (i in e.index0..e.index1)
            remove(i)
        updateUI()
    }

    override fun contentsChanged(e: ListDataEvent?) {}
    override fun dispose() {}
}