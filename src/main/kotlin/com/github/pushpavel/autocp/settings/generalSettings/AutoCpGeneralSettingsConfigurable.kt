package com.github.pushpavel.autocp.settings.generalSettings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.layout.panel
import com.github.pushpavel.autocp.common.res.AutoCpStrings.presentable
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.common.ui.dsl.simpleComboBoxView
import com.github.pushpavel.autocp.common.ui.swing.TileCellRenderer
import com.github.pushpavel.autocp.settings.langSettings.AutoCpLangSettings
import com.github.pushpavel.autocp.settings.langSettings.model.Lang

class AutoCpGeneralSettingsConfigurable : BoundConfigurable("AutoCp") {

    private val langSettings = AutoCpLangSettings.instance
    private val generalSettings = AutoCpGeneralSettings.instance

    override fun createPanel() = panel {
        commentRow(R.strings.projectSettingsOverrideMsg)
        titledRow("Solution File Generation") {

            row("Preferred Language") {
                simpleComboBoxView(
                    langSettings.languages.values.toList(),
                    { it.langId == generalSettings.getPreferredLang()?.langId },
                    { generalSettings.preferredLangId = it?.langId },
                    Lang.cellRenderer()
                )
            }
            row {
                checkBox(
                    R.strings.gatheringServiceOnStart,
                    { generalSettings.shouldStartGatheringOnStart },
                    { generalSettings.shouldStartGatheringOnStart = it }
                ).comment(R.strings.gatheringServiceOnStartDesc)
            }
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