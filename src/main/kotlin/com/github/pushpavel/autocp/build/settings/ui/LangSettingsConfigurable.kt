package com.github.pushpavel.autocp.build.settings.ui

import com.github.pushpavel.autocp.build.settings.LangSettings
import com.github.pushpavel.autocp.common.helpers.isItemsEqual
import com.github.pushpavel.autocp.common.ui.dsl.registerDslCallbacks
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import com.jetbrains.rd.util.firstOrNull

class LangSettingsConfigurable : BoundConfigurable("Languages") {
    val settings = LangSettings.instance

    override fun createPanel() = panel {
        row {
            cell(LangSettingsPanel()).apply {
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
            }.verticalAlign(VerticalAlign.FILL)
        }
    }
}