package com.github.pushpavel.autocp.settings.projectSettings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.layout.enableIf
import com.intellij.ui.layout.panel
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.common.ui.dsl.simpleComboBoxView
import com.github.pushpavel.autocp.common.ui.helpers.isSelected
import com.github.pushpavel.autocp.settings.langSettings.AutoCpLangSettings
import com.github.pushpavel.autocp.settings.langSettings.model.Lang
import com.github.pushpavel.autocp.settings.projectSettings.cmake.cmakeProjectSection

class AutoCpProjectSettingsConfigurable(val project: Project) : BoundConfigurable("Project") {
    private val langSettings = AutoCpLangSettings.instance
    private val projectSettings = project.autoCpProject()

    override fun createPanel() = panel {
        titledRow("Solution File Generation") {

            row {
                val box = checkBox(
                    "Override Preferred Language",
                    { projectSettings.overridePreferredLang },
                    { projectSettings.overridePreferredLang = it }
                )

                row("Preferred Language") {
                    simpleComboBoxView(
                        langSettings.languages.values.toList(),
                        { it.langId == projectSettings.guessPreferredLang()?.langId },
                        { projectSettings.preferredLangId = it?.langId },
                        Lang.cellRenderer()
                    )
                }.enableIf(box.isSelected())
            }
            row {

                val box = checkBox(
                    "Override ${R.strings.gatheringServiceOnStart}",
                    { projectSettings.overrideShouldStartGatheringOnStart },
                    { projectSettings.overrideShouldStartGatheringOnStart = it }
                )

                row {
                    checkBox(
                        R.strings.gatheringServiceOnStart,
                        { projectSettings.shouldStartGatheringOnStart },
                        { projectSettings.shouldStartGatheringOnStart = it }
                    ).comment(R.strings.gatheringServiceOnStartDesc)
                }.enableIf(box.isSelected())
            }

            cmakeProjectSection(project)
        }
    }

}
