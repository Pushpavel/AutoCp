package plugin.settings

import com.intellij.ide.macro.MacrosDialog
import com.intellij.openapi.util.Ref
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import javax.swing.text.PlainDocument

class LanguagePanelUI(model: Model) {
    private val nameField = ExtendableTextField(10).apply { document = model.nameDoc }
    private val extensionField = ExtendableTextField(1).apply { document = model.extensionDoc }
    private val buildCommandField = ExtendableTextField().apply { document = model.buildCommandDoc }

    val component = panel(LCFlags.fillX) {
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
        private var itemRef: Ref<SolutionLanguage?> = Ref()

        fun applyItem(item: SolutionLanguage?) {
            // replace text fields
            nameDoc.replace(0, nameDoc.length, item?.name ?: "", null)
            extensionDoc.replace(0, extensionDoc.length, item?.extension ?: "", null)
            buildCommandDoc.replace(0, buildCommandDoc.length, item?.buildCommand ?: "", null)

            setCorrespondingItem(item)
        }

        fun createItemValue(): SolutionLanguage {
            return SolutionLanguage(
                nameDoc.getText(0, nameDoc.length),
                extensionDoc.getText(0, extensionDoc.length),
                buildCommandDoc.getText(0, buildCommandDoc.length)
            )
        }

        fun setCorrespondingItem(item: SolutionLanguage?) = this.itemRef.set(item)

        fun getCorrespondingItem(): SolutionLanguage? = this.itemRef.get()
    }

}