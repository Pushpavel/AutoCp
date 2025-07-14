package com.github.pushpavel.autocp.tool.ui

import com.github.pushpavel.autocp.common.ui.layouts.html
import com.github.pushpavel.autocp.common.ui.layouts.tag
import com.github.pushpavel.autocp.database.models.SolutionFile
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBFont
import com.github.pushpavel.autocp.database.models.Testcase
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JComboBox
import javax.swing.JPanel
import kotlin.io.path.Path

fun testcaseHeader(
    model: CollectionListModel<Testcase>,
    titleLabel: JBLabel,
    testcaseIndex: () -> Int?,
): JBPanel<JBPanel<*>> {
    val jbPanel = JBPanel<JBPanel<*>>(BorderLayout())

    titleLabel.apply {
        font = JBFont.label()
        border = BorderFactory.createEmptyBorder(4, 0, 4, 0)
    }

    val panel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
    }

    panel.add(titleLabel)
    jbPanel.add(panel, BorderLayout.CENTER)


    val actionGroup = DefaultActionGroup(
        listOf(TestcaseDeleteAction(model, testcaseIndex))
    )

    jbPanel.add(
        ActionManager
            .getInstance()
            .createActionToolbar(ActionPlaces.TOOLWINDOW_CONTENT, actionGroup, true).apply {
                // TODO Nah?? deprecated API...
                // layoutPolicy = ActionToolbar.NOWRAP_LAYOUT_POLICY
                targetComponent = jbPanel
            }
            .component,
        BorderLayout.LINE_END
    )

    return jbPanel
}


class TestcaseDeleteAction(val model: CollectionListModel<Testcase>, val testcaseIndex: () -> Int?) : DumbAwareAction(
    "Delete Testcase",
    "Deletes the Testcase in Testcase viewer",
    AllIcons.General.Remove
) {

    override fun actionPerformed(e: AnActionEvent) {
        testcaseIndex()?.let {
            model.remove(it)
        }
    }

}

fun panelHeader(flow: Flow<SolutionFile?>, scope: CoroutineScope): DialogPanel = panel {
    indent {
        row {
            val icon = label("").component
            val title = label(html { tag("h2", "...") }).component

            scope.launch {
                flow.filterNotNull().collect {
                    val fileName = Path(it.pathString).fileName.toString()
                    // icon of the file type of the solution file
                    icon.icon = FileTypeManager.getInstance()
                        .getFileTypeByFileName(fileName).icon

                    title.text = html { tag("h2", fileName) }
                }
            }
        }
    }
}

fun programEditorHeader(lb: String, combobox: JComboBox<String>): DialogPanel = panel {
    row {
        label(lb)
        contextHelp("leave empty to disable")
        cell(combobox).align(AlignX.RIGHT)
    }
}
