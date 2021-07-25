package settings.generalSettings.ui

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBPanel
import com.intellij.ui.layout.panel
import kotlinx.coroutines.launch
import settings.langSettings.model.Lang
import ui.helpers.viewScope
import ui.vvm.swingBinding.bind
import java.awt.BorderLayout

class GeneralSettingsView(viewModel: GeneralSettingsViewModel) : JBPanel<GeneralSettingsView>(BorderLayout()) {
    val scope = viewScope(viewModel.scope)
    val langComboBox = ComboBox<Lang>()

    init {
        langComboBox.renderer = Lang.cellRenderer()

        val container = panel {
            row("Preferred Language") {
                langComboBox()
            }
        }

        add(container, BorderLayout.CENTER)

        scope.launch {
            bind(
                langComboBox,
                viewModel.langList,
                viewModel.selectedLangIndex
            )
        }
    }
}