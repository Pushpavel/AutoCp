package tool

import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBLabel
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import files.ProblemSpec
import files.TestcaseSpec
import tool.base.ContentAdapter
import ui.poplist.PopListModel

class ProblemViewer : ContentAdapter<ProblemSpec>(), FileEditorManagerListener {

    val model = PopListModel<TestcaseSpec>()
    private val panel = TestcasesPanel(model)

    private val emptyContent = contentFactory.createContent(JBLabel("Content empty!"), "Problem", false)

    override fun createContentForData(data: ProblemSpec, contentFactory: ContentFactory): Content {
        model.listModel.replaceAll(data.testcases)
        return contentFactory.createContent(panel.component, data.name, false)
    }

    override fun getEmptyContent() = emptyContent

}