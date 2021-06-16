package gather.ui

import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.layout.panel
import com.intellij.ui.treeStructure.Tree
import settings.SolutionLanguage
import java.awt.BorderLayout

fun createGenerateSolutionsDialog(model: GenerateSolutionsDialogModel): DialogBuilder {

    val dialog = DialogBuilder(model.project)

    dialog.apply {
        title("Generate Solution Files")
        resizable(false)

        centerPanel(
            JBPanelWithEmptyText(BorderLayout())
                .withPreferredWidth(300)
                .withMinimumHeight(100)
                .withEmptyText("Add at least one solution language in settings").apply {
                    if (model.isValid) {

                        val comboBoxComponent = ComboBox(model.langModel).apply {
                            renderer = SolutionLanguage.cellRenderer()
                        }

                        val ui = panel {
                            blockRow { Tree(model.treeModel)() }
                            row("Language:") {
                                comboBoxComponent()
                            }
                        }

                        add(ui)
                        comboBoxComponent.addActionListener {
                            dialog.setOkActionEnabled(model.langModel.selected != null)
                        }
                    }
                }
        )
    }

    dialog.setOkActionEnabled(model.isValid && model.langModel.selected != null)

    return dialog
}