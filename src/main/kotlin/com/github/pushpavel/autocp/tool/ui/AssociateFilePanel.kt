package com.github.pushpavel.autocp.tool.ui

import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.applyToComponent
import com.intellij.ui.layout.panel
import com.intellij.util.ui.components.BorderLayoutPanel
import com.github.pushpavel.autocp.common.ui.layouts.html
import com.github.pushpavel.autocp.common.ui.layouts.tag
import java.awt.BorderLayout

class AssociateFilePanel(fileName: String, val enableCallback: () -> Unit) {

    private val header: DialogPanel = panel {
        row {
            placeholder().constraints(growX, pushX)
            cell {
                label("").applyToComponent {
                    icon = FileTypeManager.getInstance()
                        .getFileTypeByFileName(fileName).icon
                }
                label(html { tag("h2", fileName) }).component
            }
            placeholder().constraints(growX, pushX)
        }
    }

    val component = BorderLayoutPanel().apply {
        add(panel(LCFlags.fill) {
            row { placeholder().constraints(growY, pushY) }
            row {
                placeholder().constraints(growX, pushX)
                header()
                placeholder().constraints(growX, pushX)
            }
            row {
                placeholder().constraints(growX, pushX)
                button("Enable") { enableCallback() }
                placeholder().constraints(growX, pushX)
            }
            row { placeholder().constraints(growY, pushY) }
        }, BorderLayout.CENTER)
    }
}