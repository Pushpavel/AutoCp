package plugin.settings

import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.SingleSelectionModel

class SettingsModel : CollectionComboBoxModel<SolutionLanguage>(
    mutableListOf(
        SolutionLanguage("cpp", "g++", ".cpp"),
        SolutionLanguage("java", "javac", ".java"),
        SolutionLanguage("python", "javac", ".java"),
        SolutionLanguage("kotlin", "javac", ".java"),
    )
) {

    val languagePanelModel = LanguagePanelUI.Model()
    val sideListSelectionModel = SingleSelectionModel()

    init {
        sideListSelectionModel.addListSelectionListener {
            print("Selection Changed ${sideListSelectionModel.anchorSelectionIndex} \n")
        }
    }
}