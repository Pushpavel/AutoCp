package settings.langSettings.ui.langItem

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import settings.langSettings.model.Lang
import ui.dsl.comboBoxView
import ui.helpers.afterChange
import ui.swing.TileCellRenderer

class LangItemPanel {

    var selectedLang: Lang? by afterChange(null, ::onSelectedLangChange)

    private val fileTemplatesModel = CollectionComboBoxModel<FileTemplate>()

    val dialogPanel by lazy {
        panel(LCFlags.fill) {
            row {
                row("File Template") {
                    comboBoxView(
                        fileTemplatesModel,
                        { it.name == selectedLang?.fileTemplateName },
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
            }
        }
    }

    private fun onSelectedLangChange(selectedLang: Lang?) {
        if (selectedLang == null) {
            fileTemplatesModel.removeAll()
            return
        }

        val fileType = Language.findLanguageByID(selectedLang.langId)?.associatedFileType!!
        val manager = FileTemplateManager.getDefaultInstance()
        val fileTemplates = listOf(
            *manager.allJ2eeTemplates,
            *manager.allTemplates,
            *manager.internalTemplates
        ).filter { template -> template.isTemplateOfType(fileType) }

        fileTemplatesModel.replaceAll(fileTemplates)
    }


}