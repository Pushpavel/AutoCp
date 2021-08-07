package settings.langSettings

import com.intellij.ide.ui.fullRow
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import common.helpers.isItemsEqual
import settings.langSettings.ui.LangSettingsPanel
import ui.dsl.registerDslCallbacks

class AutoCpLangSettingsConfigurable : BoundConfigurable("Languages") {

    val settings = AutoCpLangSettings.instance

    override fun createPanel() = panel(LCFlags.fill) {
        fullRow {
            LangSettingsPanel()().apply {
                constraints(CCFlags.push, CCFlags.grow)

                onReset {
                    component.apply {
                        langListModel.replaceAll(settings.languages)
                        sideList.setSelectedValue(settings.languages.firstOrNull(), false)
                    }
                }

                registerDslCallbacks()

                onIsModified {
                    !component.langListModel.items.isItemsEqual(settings.languages)
                }

                onApply {
                    component.apply {
                        settings.languages = langListModel.items
                    }
                }
            }
        }
    }
}