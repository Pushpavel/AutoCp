package tool

import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.ui.components.JBLabel
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import database.models.Testcase
import com.github.pushpavel.autocp.database.Problem
import tool.base.ContentAdapter
import ui.poplist.PopListModel

/**
 * Creates new ToolWindow Content whenever currently selected file changes
 */
class ProblemViewer : ContentAdapter<Problem>(), FileEditorManagerListener {

    val model = object : PopListModel<Testcase>() {
        override val itemNameRegex = Regex("^(.*) #([0-9]+)\$")

        override fun getItemName(item: Testcase) = item.name
        override fun buildItemName(name: String, suffix: String) = "$name #$suffix"

        override fun createNewItem(from: Testcase?): Testcase {
            if (from != null)
                return from.copy(name = nextUniqueName(from.name))
            return Testcase(name = "Testcase #0", "", "")
        }
    }
    private val panel = TestcasesPanel(model)

    private val emptyContent = contentFactory.createContent(JBLabel("Content empty!"), "Problem", false)

    override fun createContentForData(data: Problem, contentFactory: ContentFactory): Content {
        model.listModel.replaceAll(data.testcases)
        model.setSelectionIndex(data.selectedTestcaseIndex.toInt().let {
            if (it == -1 && data.testcases.isNotEmpty()) 0 else it
        })
        return contentFactory.createContent(panel.component, data.name, false)
    }

    override fun getEmptyContent() = emptyContent

}