package settings

import com.intellij.ide.macro.MacrosDialog
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import plugin.settings.ErrorComponent

class LanguageItemPanel(model: LanguageItemModel) {

    private val nameField = ExtendableTextField(10).apply { document = model.nameDoc }
    private val extensionField = ExtendableTextField(1).apply { document = model.extensionDoc }
    private val buildCommandField = ExtendableTextField(50).apply { document = model.buildCommandDoc }

    val component = panel(LCFlags.fillX) {
        // textField features
        MacrosDialog.addTextFieldExtension(buildCommandField)

        // ui & layout
        row {
            row("Name:") { nameField() }
            row("Extension:") { extensionField() }
            row("Build Command:") {
                buildCommandField(pushX).comment(
                    "This command will be executed to build the executable of your solution code.<br>" +
                            "@input@ will be replaced with \"path/to/input/file\" without quotes<br>" +
                            "@output@ will be replaced with \"path/to/output/file\" without quotes"
                )
            }
            blockRow { }
            row { ErrorComponent(model.nameError)() }
            row { ErrorComponent(model.extensionError)() }
            row { ErrorComponent(model.buildCommandError)() }
        }
    }

}