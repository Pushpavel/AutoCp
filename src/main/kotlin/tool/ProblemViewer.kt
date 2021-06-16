package tool

import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.ui.components.JBLabel
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import database.models.Testcase
import dev.pushpavel.autocp.database.Problem
import tool.base.ContentAdapter
import ui.poplist.PopListModel

class ProblemViewer : ContentAdapter<Problem>(), FileEditorManagerListener {

    val model = PopListModel<Testcase>()
    private val panel = TestcasesPanel(model)

    private val emptyContent = contentFactory.createContent(JBLabel("Content empty!"), "Problem", false)

    override fun createContentForData(data: Problem, contentFactory: ContentFactory): Content {
        model.listModel.replaceAll(data.testcases)
        return contentFactory.createContent(panel.component, data.name, false)
    }

    override fun getEmptyContent() = emptyContent

}