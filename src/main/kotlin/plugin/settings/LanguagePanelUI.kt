package plugin.settings

import com.intellij.ide.macro.MacrosDialog
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.panel
import javax.swing.text.PlainDocument

class LanguagePanelUI(model: Model) {
    private val nameField = ExtendableTextField(10).apply { document = model.nameDoc }
    private val extensionField = ExtendableTextField(1).apply { document = model.extensionDoc }
    private val buildCommandField = ExtendableTextField().apply { document = model.buildCommandDoc }

    val component = panel {
        MacrosDialog.addTextFieldExtension(buildCommandField)
        row {
            row("Name:") { nameField() }
            row("Extension:") { extensionField() }.largeGapAfter()
            row("Build Command:") { buildCommandField(growX) }
        }
    }

    class Model {
        internal val nameDoc = PlainDocument()
        internal val extensionDoc = PlainDocument()
        internal val buildCommandDoc = PlainDocument()
        private var index: Int? = null

        fun replaceModel(data: SolutionLanguage?, index: Int?) {
            // replace text fields
            nameDoc.replace(0, nameDoc.length, data?.name ?: "", null)
            extensionDoc.replace(0, extensionDoc.length, data?.extension ?: "", null)
            buildCommandDoc.replace(0, buildCommandDoc.length, data?.buildCommand ?: "", null)

            this.index = index
        }

        fun getModel(): SolutionLanguage {
            return SolutionLanguage(
                nameDoc.getText(0, nameDoc.length),
                extensionDoc.getText(0, extensionDoc.length),
                buildCommandDoc.getText(0, buildCommandDoc.length),
            )
        }

        fun getIndex(): Int? = index
    }

}