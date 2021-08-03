package gather.ui.solutionsDialog

import com.github.pushpavel.autocp.database.Problem
import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.CollectionListModel
import settings.generalSettings.AutoCpGeneralSettings
import settings.langSettings.AutoCpLangSettings
import javax.swing.Icon

class SolutionsDialogModel(val project: Project, problems: List<Problem>) {
    val listModel = CollectionListModel(problems)
    val groupName = problems.firstOrNull()?.groupName

    private val generalSettings = AutoCpGeneralSettings.instance
    private val langSettings = AutoCpLangSettings.instance

    val langModel = CollectionComboBoxModel(langSettings.languages, run {
        langSettings.languages.firstOrNull { it.langId == generalSettings.preferredLangId }
    })
    var langIcon: Icon? = null
}