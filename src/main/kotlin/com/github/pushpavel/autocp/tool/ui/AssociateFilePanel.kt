package com.github.pushpavel.autocp.tool.ui

import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.ui.DialogPanel
import com.intellij.util.ui.components.BorderLayoutPanel
import com.github.pushpavel.autocp.common.ui.layouts.html
import com.github.pushpavel.autocp.common.ui.layouts.tag
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign
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
                cell(header).horizontalAlign(HorizontalAlign.CENTER).verticalAlign(VerticalAlign.CENTER)
            }
            row {
                button("Enable") { enableCallback() }.horizontalAlign(HorizontalAlign.CENTER).verticalAlign(VerticalAlign.CENTER)
            }
        }, BorderLayout.CENTER)
    }
}