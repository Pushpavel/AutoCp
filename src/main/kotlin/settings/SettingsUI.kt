package settings

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBPanel
import com.intellij.ui.layout.panel
import java.awt.BorderLayout

class SettingsUI(model: SettingsUIModel) : JBPanel<SettingsUI>(BorderLayout()) {

    val preferredLangBox = ComboBox(model.preferredLangModel).apply {
        renderer = SolutionLanguage.cellRenderer()
    }

    init {
        add(panel {
            row("Preferred Language:") {
                preferredLangBox()
            }
        }, BorderLayout.NORTH)

        add(
            LanguagesPopList(model.popListModel).component,
            BorderLayout.CENTER
        )
    }


}