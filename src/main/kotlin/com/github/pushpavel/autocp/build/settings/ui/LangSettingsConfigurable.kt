package com.github.pushpavel.autocp.build.settings.ui

import com.github.pushpavel.autocp.build.settings.LangSettings
import com.github.pushpavel.autocp.common.helpers.isItemsEqual
import com.github.pushpavel.autocp.common.ui.dsl.registerDslCallbacks
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import com.jetbrains.rd.util.firstOrNull

class LangSettingsConfigurable : BoundConfigurable("Languages") {
    val settings = LangSettings.instance

    override fun createPanel() = panel(LCFlags.fill) {

        row {
            cell(isFullWidth = true) {
                LangSettingsPanel()().apply {
                    constraints(CCFlags.push, CCFlags.grow)
                    onReset {
                        component.apply {
                            langListModel.replaceAll(settings.langs.values.toList())
                            sideList.setSelectedValue(settings.langs.firstOrNull()?.value, false)
                        }
                    }

                    registerDslCallbacks()

                    onIsModified {
                        !component.langListModel.items.isItemsEqual(settings.langs.values)
                    }

                    onApply {
                        component.apply {
                            settings.updateLangs(langListModel.items.associateBy { it.extension })
                        }
                    }
                }
            }
        }
    }
}