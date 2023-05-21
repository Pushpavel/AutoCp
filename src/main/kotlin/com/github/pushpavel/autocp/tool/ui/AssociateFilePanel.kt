package com.github.pushpavel.autocp.tool.ui

import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.ui.DialogPanel
import com.intellij.util.ui.components.BorderLayoutPanel
import com.github.pushpavel.autocp.common.ui.layouts.html
import com.github.pushpavel.autocp.common.ui.layouts.tag
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.AlignY
import com.intellij.ui.dsl.builder.panel
import java.awt.BorderLayout

class AssociateFilePanel(fileName: String, val enableCallback: () -> Unit) {

    private val header: DialogPanel = panel {
        indent {
            row {
                label("").applyToComponent {
                    icon = FileTypeManager.getInstance()
                        .getFileTypeByFileName(fileName).icon
                }
                label(html { tag("h2", fileName) })
            }
        }
    }

    val component = BorderLayoutPanel().apply {
        add(panel {
            row {
                cell(header).align(AlignX.CENTER).align(AlignY.CENTER)
            }
            row {
                button("Enable") { enableCallback() }.align(AlignX.CENTER).align(AlignY.CENTER)
            }
        }, BorderLayout.CENTER)
    }
}