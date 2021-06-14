package gather.ui

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.layout.panel
import com.intellij.ui.treeStructure.Tree
import database.models.ProblemInfo
import gather.models.GenerateSolutionsContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import plugin.settings.AutoCpSettings
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeModel

class GenerateSolutionsDialog(project: Project, private val problems: List<ProblemInfo>) :
    DialogWrapper(project, false) {

    private val settings = project.service<AutoCpSettings>()
    private val langModel = CollectionComboBoxModel(
        settings.solutionLanguages,
        settings.solutionLanguages.find { it.name == settings.preferredLanguage }
            ?: settings.solutionLanguages.takeIf { it.size != 0 }?.get(0)
    )

    private val treeModel = createProblemsTreeModel()

    init {
        title = "Generate Solution Files"
        setResizable(false)
    }

    override fun createCenterPanel() = panel {
        blockRow { Tree(treeModel) }
        row("Language:") { comboBox(langModel, { null }, {}) }
    }


    private fun createProblemsTreeModel(): TreeModel {
        val rootNode = DefaultMutableTreeNode(problems[0].group)

        for (it in problems) {
            val node = DefaultMutableTreeNode(it.name)
            rootNode.add(node)
        }

        return DefaultTreeModel(rootNode)
    }

    suspend fun getResult() = withContext(Dispatchers.Main) {
        if (!showAndGet())
            return@withContext null

        //Fixme: This will break if no solution languages are defined
        GenerateSolutionsContext(problems, langModel.selected!!)
    }

}