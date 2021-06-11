package tool

import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.ui.components.JBLabel
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import database.models.ProblemData
import database.models.TestcaseSpec
import tool.base.ContentAdapter
import ui.poplist.PopListModel

class ProblemViewer : ContentAdapter<ProblemData>(), FileEditorManagerListener {

    val model = PopListModel<TestcaseSpec>()
    private val panel = TestcasesPanel(model)

    private val emptyContent = contentFactory.createContent(JBLabel("Content empty!"), "Problem", false)

    override fun createContentForData(data: ProblemData, contentFactory: ContentFactory): Content {
        model.listModel.replaceAll(data.testcases)
        return contentFactory.createContent(panel.component, data.spec.name, false)
    }

    override fun getEmptyContent() = emptyContent

}