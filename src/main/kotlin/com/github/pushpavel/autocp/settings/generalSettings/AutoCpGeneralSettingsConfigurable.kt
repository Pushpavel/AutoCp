package com.github.pushpavel.autocp.settings.generalSettings

import com.github.pushpavel.autocp.common.res.AutoCpStrings.presentable
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.common.ui.dsl.simpleComboBoxView
import com.github.pushpavel.autocp.common.ui.swing.TileCellRenderer
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.layout.panel
import java.nio.file.InvalidPathException
import kotlin.io.path.Path
import kotlin.io.path.pathString

class AutoCpGeneralSettingsConfigurable : BoundConfigurable("AutoCp") {

    private val generalSettings = AutoCpGeneralSettings.instance
    var rootDir = generalSettings.fileGenerationRoot
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
            row("File Generation Root") {
                textField(::rootDir)
                    .onReset { rootDir = generalSettings.fileGenerationRoot }
                    .onIsModified { rootDir != generalSettings.fileGenerationRoot }
                    .onApply {
                        try {
                            generalSettings.fileGenerationRoot = if (rootDir.isNotBlank())
                                Path(rootDir).pathString
                            else ""
                        } catch (e: InvalidPathException) {
                            // ignored
                        }
                    }.withValidationOnInput {
                        try {
                            if (it.text.isNotBlank())
                                Path(it.text)
                            null
                        } catch (e: InvalidPathException) {
                            error(e.localizedMessage)
                        }
                    }.comment(R.strings.fileGenerationRootComment)
            }
        }
    }
}