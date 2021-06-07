package plugin.settings

import com.intellij.openapi.options.Configurable

class SettingsConfigurable : Configurable {

    val ui by lazy { SettingsUI() }

    override fun createComponent() = ui

    override fun isModified(): Boolean {
        return false
    }

    override fun apply() {
    }

    override fun getDisplayName() = "AutoCp"
}