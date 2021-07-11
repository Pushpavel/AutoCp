package gather.ui

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionComboBoxModel
import com.github.pushpavel.autocp.database.Problem
import settings.AutoCpSettings
import settings.SolutionLanguage
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeModel

/**
 * ViewModel for dialog that generates solution files
 */
class GenerateSolutionsDialogModel(val project: Project, problems: List<Problem>) {
    private val settings = project.service<AutoCpSettings>()
    val langModel: CollectionComboBoxModel<SolutionLanguage>
    val treeModel: TreeModel
    val isValid: Boolean

    init {
        val selectedLang = settings.getPreferredLang() ?: settings.solutionLanguages.firstOrNull()

        isValid = problems.isNotEmpty()

        langModel = CollectionComboBoxModel(settings.solutionLanguages, selectedLang)

        val rootNode = DefaultMutableTreeNode(
            if (problems.isNotEmpty())
                problems[0].groupName
            else
                "No CP Problems"
        )

        for (it in problems) {
            val node = DefaultMutableTreeNode(it.name)
            rootNode.add(node)
        }

        treeModel = DefaultTreeModel(rootNode)

    }
}