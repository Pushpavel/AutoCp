package plugin.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel
import com.jetbrains.rd.framework.base.deepClonePolymorphic

class AutoCpConfigurable : Configurable {
    private var ui: AutoCpSettingsUI? = null
    override fun createComponent(): DialogPanel {
        AutoCpSettingsUI().also {
            ui = it
            return it.component
        }
    }

    override fun isModified(): Boolean {
        val settings = AutoCpSettings.instance

        ui?.let {
            return settings.preferredLanguage != it.comboBoxModel.selected
                    || settings.solutionLanguages.zip(it.listModel.items).any { p ->
                print("{$p}:{${p.first != p.second}}\n")
                p.first != p.second
            }.also { r ->
                print(r)
            }
                    || settings.selectedIndex != it.listModel.getSelectedIndex()
        }


        return false
    }

    override fun apply() {
        val settings = AutoCpSettings.instance
        ui?.let {
            settings.preferredLanguage = it.comboBoxModel.selected
            settings.solutionLanguages = it.listModel.items
            settings.selectedIndex = it.listModel.getSelectedIndex()
        }
    }

    override fun reset() {
        val settings = AutoCpSettings.instance
        ui?.let {
            it.comboBoxModel.removeAll()
            it.comboBoxModel.addAll(0, settings.solutionLanguages.map { sol -> sol.name })
            it.comboBoxModel.selectedItem = settings.preferredLanguage

            it.listModel.removeAll()
            it.listModel.add(settings.solutionLanguages)
            it.listModel.setSelectedIndex(settings.selectedIndex)
        }
    }

    override fun disposeUIResources() {
        ui = null
    }

    override fun getDisplayName() = "AutoCp Build Tools"
}