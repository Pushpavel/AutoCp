package settings

import com.intellij.openapi.options.Configurable

/**
 * [Configurable] that connects view [SettingsUI] to its viewModel [SettingsUIModel]
 * and handles saving, resetting and initializing the UI
 */
class SettingsConfigurable : Configurable {
    var model: SettingsUIModel? = null

    override fun createComponent(): SettingsUI {
        val model = SettingsUIModel()
        this.model = model
        return SettingsUI(model)
    }

    override fun isModified(): Boolean {
        val settings = AutoCpSettings.instance

        return model?.run {
            settings.selectedIndex != popListModel.selectionModel.minSelectionIndex
                    || settings.getPreferredLang() != preferredLangModel.selectedItem
                    || settings.solutionLanguages != popListModel.listModel.items // TODO: compare individually
        } ?: false
    }

    override fun apply() {
        val settings = AutoCpSettings.instance
        model?.run {
            settings.solutionLanguages = popListModel.listModel.items
            settings.selectedIndex = popListModel.selectionModel.minSelectionIndex
            settings.setPreferredLang(preferredLangModel.selectedItem)
        }
    }

    override fun reset() {
        val settings = AutoCpSettings.instance
        model?.run {
            popListModel.listModel.replaceAll(settings.solutionLanguages)
            popListModel.setSelectionIndex(settings.selectedIndex ?: -1) //TODO: remove ?: -1 after refactoring
            preferredLangModel.selectedItem = settings.getPreferredLang()
        }
    }

    override fun getDisplayName() = "AutoCp"
}