package plugin.settings

import com.intellij.openapi.options.Configurable

class SettingsConfigurable : Configurable {

    var ui: SettingsUI? = null

    override fun createComponent() = SettingsUI().also { ui = it }


    override fun isModified(): Boolean {
        val model = ui?.model ?: return false
        val settings = AutoCpSettings.instance
        // saving changes in language model to list model
        model.applyLanguagePanelModelToListModel()

        return (model.selected?.name != settings.preferredLanguage) // Preferred Language
                || model.sideListSelectionModel.minSelectionIndex != settings.selectedIndex // side-list selected index
                || model.items.zip(settings.solutionLanguages)
            .any {
                print(
                    "(${it.first.name}:${it.second.name})\n" +
                            "(${it.first.extension},${it.second.extension})\n" +
                            "(${it.first.buildCommand},${it.second.buildCommand})\n\n"
                )
                !it.first.equals(it.second)
            } //  list items
    }

    override fun apply() {
        val model = ui?.model ?: return
        val settings = AutoCpSettings.instance

        // saving changes in language model to list model
        model.applyLanguagePanelModelToListModel()
        settings.solutionLanguages = model.items.map { it.copy() }.toMutableList()
        settings.preferredLanguage = model.selected?.name
        settings.selectedIndex = model.sideListSelectionModel.minSelectionIndex.takeIf { it != -1 }

    }

    override fun reset() {
        val model = ui?.model ?: return
        val settings = AutoCpSettings.instance

        model.removeAll()
        model.addAll(0, settings.solutionLanguages.map { it.copy() })

        model.selectedItem = settings.solutionLanguages.find { it.name == settings.preferredLanguage }

        settings.selectedIndex?.also {
            if (it < model.size)
                model.sideListSelectionModel.setSelectionInterval(it, it)
        } ?: model.sideListSelectionModel.clearSelection()

        // saving changes in list model to language model
        model.applyListModelToLanguagePanelModel()
    }

    override fun getDisplayName() = "AutoCp"

    override fun disposeUIResources() {
        ui = null
    }
}