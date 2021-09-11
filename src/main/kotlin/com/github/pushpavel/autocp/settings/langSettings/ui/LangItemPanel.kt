package com.github.pushpavel.autocp.settings.langSettings.ui

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.SingleSelectionModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.InnerCell
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import com.github.pushpavel.autocp.common.helpers.UniqueNameEnforcer
import com.github.pushpavel.autocp.common.helpers.isItemsEqual
import com.github.pushpavel.autocp.common.ui.dsl.DslCallbacks
import com.github.pushpavel.autocp.common.ui.dsl.comboBoxView
import com.github.pushpavel.autocp.common.ui.swing.TileCellRenderer
import com.github.pushpavel.autocp.gather.supportedFileTemplates
import com.github.pushpavel.autocp.settings.langSettings.model.BuildConfig
import com.github.pushpavel.autocp.settings.langSettings.model.Lang
import com.github.pushpavel.autocp.settings.langSettings.ui.dialogs.BuildConfigDialog

class LangItemPanel : DslCallbacks {

    var selectedLang: Lang? = null

    private val fileTemplatesModel = CollectionComboBoxModel<FileTemplate>()
    private val buildConfigsModel = CollectionComboBoxModel<BuildConfig>()

    private val buildConfigNameEnforcer = UniqueNameEnforcer(
        Regex("^(.*) \\(([0-9]+)\\)\$"),
        { p, s -> "$p ($s)" },
        { buildConfigsModel.items.map { it.name } }
    )

    val dialogPanel = panel(LCFlags.fill) {
        row("File Template") {
            comboBoxView(
                fileTemplatesModel,
                { it.name == selectedLang?.getFileTemplate()?.name },
                {
                    selectedLang?.apply {
                        selectedLang = copy(fileTemplateName = it?.name)
                    }
                },
                TileCellRenderer {
                    text = it.name
                    icon = FileTypeManager.getInstance().getFileTypeByExtension(it.extension).icon
                }
            )
        }

        row("Default Build Configuration") {
            comboBoxView(
                buildConfigsModel,
                { it.id == selectedLang?.getDefaultBuildConfig()?.id },
                {
                    selectedLang?.apply {
                        selectedLang = copy(defaultBuildConfigId = it?.id)
                    }
                },
                BuildConfig.cellRenderer()
            )
        }

        titledRow("Build Configurations") {
            subRowIndent = 0
            row {
                cell(isFullWidth = true) {

                    buildConfigsList()
                }
            }
        }
    }

    private fun InnerCell.buildConfigsList() {
        val jbList = JBList(buildConfigsModel).apply {
            selectionModel = SingleSelectionModel()
            cellRenderer = BuildConfig.cellRenderer()
        }

        val listContainer = ToolbarDecorator.createDecorator(jbList).setAddAction {
            val blank = BuildConfig(System.currentTimeMillis().toString(), "", "", "")
            val newBuildConfig = BuildConfigDialog(blank, buildConfigNameEnforcer, true).showAndGetConfig()
            if (newBuildConfig != null) {
                buildConfigsModel.add(newBuildConfig)
                jbList.selectedIndex = buildConfigsModel.items.size - 1
            }
        }.setEditAction {
            val config = BuildConfigDialog(jbList.selectedValue, buildConfigNameEnforcer, false).showAndGetConfig()
            if (config != null)
                buildConfigsModel.setElementAt(config, jbList.selectedIndex)
        }.createPanel()


        listContainer(CCFlags.grow, CCFlags.push)
    }

    override fun isModified(): Boolean {
        return dialogPanel.isModified() || !buildConfigsModel.items.isItemsEqual(
            selectedLang?.buildConfigs?.values ?: listOf()
        )
    }

    override fun reset() {
        val selectedLang = selectedLang
        if (selectedLang == null) {
            fileTemplatesModel.removeAll()
            buildConfigsModel.removeAll()
        } else {
            fileTemplatesModel.replaceAll(selectedLang.supportedFileTemplates())
            buildConfigsModel.replaceAll(selectedLang.buildConfigs.values.toList())
        }

        dialogPanel.reset()
    }

    override fun apply() {
        selectedLang?.apply {
            selectedLang = copy(buildConfigs = buildConfigsModel.items.associateBy { it.id })
            dialogPanel.apply()
        }
    }
}