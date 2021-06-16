package settings

import com.intellij.ide.macro.MacrosDialog
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel

class LanguageItemPanel(model: LanguageItemModel) {

    private val nameField = ExtendableTextField(10).apply { document = model.nameDoc }
    private val extensionField = ExtendableTextField(1).apply { document = model.extensionDoc }
    private val buildCommandField = ExtendableTextField(50).apply { document = model.buildCommandDoc }

    val component = panel(LCFlags.fillX) {
        // textField features
        MacrosDialog.addTextFieldExtension(buildCommandField)

        // ui & layout
        blockRow {
            row("Name:") {
                nameField()
            }
            row("Extension:") {
                extensionField()
            }.largeGapAfter()
            row("Build Command:") {
                buildCommandField(pushX)
            }
        }
    }

}