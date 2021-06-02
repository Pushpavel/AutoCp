package plugin.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import com.intellij.ui.treeStructure.Tree
import common.AutoCpProblem
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class GatherProblemsDialogUI {

    fun getGatheringDialog(project: Project?): DialogBuilder {
        val dialog = DialogBuilder(project)
        dialog.setTitle("Gathering Problems")
        dialog.setCenterPanel(panel {
            row {
                label("Open browser and parse problem or contest using competitive companion extension")
            }
            row {
                label("Listening for competitive companion...")
            }
        })
        dialog.removeAllActions()
        // adding stop button
        dialog.addCloseButton().setText("Stop")

        return dialog
    }

    fun getGenerateFilesDialog(project: Project?, problems: List<AutoCpProblem>): DialogBuilder {
        val dialog = DialogBuilder(project)
        dialog.setTitle("Generate Solution Files")
        val rootNode = DefaultMutableTreeNode(problems[0].group)

        for (it in problems) {
            val node = DefaultMutableTreeNode(it.name)
            rootNode.add(node)
        }

        val treeModel = DefaultTreeModel(rootNode)
        val tree = Tree(treeModel)

        dialog.setCenterPanel(panel {
            row {
                tree()
            }
        })

        return dialog
    }

    fun closeDialog(dialog: DialogBuilder?) {
        dialog?.let {
            if (!it.dialogWrapper.isDisposed)
                it.dialogWrapper.close(DialogWrapper.CLOSE_EXIT_CODE)
        }
    }

}