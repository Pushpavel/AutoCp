package tool.ui

import com.intellij.ui.CollectionListModel
import database.models.SolutionFile
import database.models.Testcase
import ui.swing.editableList.EditableListView

fun testcaseListPanel(solutionFile: SolutionFile): EditableListView<Testcase> {
    val testcaseListModel = CollectionListModel(solutionFile.testcases)

    return EditableListView(testcaseListModel) {
        TestcasePanel()
    }
}


