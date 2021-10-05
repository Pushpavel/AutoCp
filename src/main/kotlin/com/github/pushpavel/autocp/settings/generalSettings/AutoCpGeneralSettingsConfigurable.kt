package com.github.pushpavel.autocp.settings.generalSettings

import com.github.pushpavel.autocp.common.res.AutoCpStrings.presentable
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.common.ui.dsl.simpleComboBoxView
import com.github.pushpavel.autocp.common.ui.swing.TileCellRenderer
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.layout.panel

class AutoCpGeneralSettingsConfigurable : BoundConfigurable("AutoCp") {

    private val generalSettings = AutoCpGeneralSettings.instance

    override fun createPanel() = panel {
        titledRow("Solution File Generation") {
            row(R.strings.openFilesOnGatherText) {
                simpleComboBoxView(
                    OpenFileOnGather.values().asList(),
                    { it == generalSettings.openFilesOnGather },
                    { generalSettings.openFilesOnGather = it!! },
                    TileCellRenderer { text = it.presentable() }
                )
            }
        }
    }
}