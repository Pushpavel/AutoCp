package gather.ui.solutionsDialog

import com.github.pushpavel.autocp.database.Problem
import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.CollectionListModel
import settings.langSettings.AutoCpLangSettings
import javax.swing.Icon

class SolutionsDialogModel(val project: Project, problems: List<Problem>) {
    val listModel = CollectionListModel(problems)
    val groupName = problems.firstOrNull()?.groupName
    val langModel = CollectionComboBoxModel(AutoCpLangSettings.getLanguages())
    var langIcon: Icon? = null
}