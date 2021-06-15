package tool

import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.ui.components.JBLabel
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import database.models.OldProblemSpec
import database.models.TestcaseSpec
import tool.base.ContentAdapter
import ui.poplist.PopListModel

class ProblemViewer : ContentAdapter<OldProblemSpec>(), FileEditorManagerListener {

    val model = PopListModel<TestcaseSpec>()
    private val panel = TestcasesPanel(model)

    private val emptyContent = contentFactory.createContent(JBLabel("Content empty!"), "Problem", false)

    override fun createContentForData(spec: OldProblemSpec, contentFactory: ContentFactory): Content {
        model.listModel.replaceAll(spec.testcases)
        return contentFactory.createContent(panel.component, spec.info.name, false)
    }

    override fun getEmptyContent() = emptyContent

}