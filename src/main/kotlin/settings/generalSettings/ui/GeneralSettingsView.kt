package settings.generalSettings.ui

import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.layout.panel
import settings.langSettings.model.Lang
import java.awt.BorderLayout

class GeneralSettingsView : JBPanel<GeneralSettingsView>(BorderLayout()) {
    var selectedLang: Lang? = null
    val langModel = CollectionComboBoxModel<Lang>()

    init {

        val container = panel {
            row("Preferred Language") {
                comboBox<Lang?>(
                    langModel,
                    { selectedLang },
                    { selectedLang = it },
                    Lang.cellRenderer()
                )
            }
        }

        add(container, BorderLayout.CENTER)
    }
}