package settings.langSettings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import com.jetbrains.rd.util.firstOrNull
import common.helpers.isItemsEqual
import common.ui.dsl.registerDslCallbacks
import settings.langSettings.ui.LangSettingsPanel

class AutoCpLangSettingsConfigurable : BoundConfigurable("Languages") {

    val settings = AutoCpLangSettings.instance

    override fun createPanel() = panel(LCFlags.fill) {
        row {
            cell(isFullWidth = true) {

                LangSettingsPanel()().apply {
                    constraints(CCFlags.push, CCFlags.grow)

                    onReset {
                        component.apply {
                            langListModel.replaceAll(settings.languages.values.toList())
                            sideList.setSelectedValue(settings.languages.firstOrNull()?.value, false)
                        }
                    }

                    registerDslCallbacks()

                    onIsModified {
                        !component.langListModel.items.isItemsEqual(settings.languages.values)
                    }

                    onApply {
                        component.apply {
                            settings.languages = langListModel.items.associateBy { it.langId }
                        }
                    }
                }
            }
        }
    }
}
