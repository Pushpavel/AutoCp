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

        // textField features
        MacrosDialog.addTextFieldExtension(buildCommandField)

        // ui & layout
        row("Name:") {
            nameField()
            model.nameErrorComponent()
        }
        row("Extension:") {
            extensionField()
            model.extensionErrorComponent()
        }.largeGapAfter()
        row("Build Command:") {
            buildCommandField(pushX)
            model.buildCommandErrorComponent()
        }
    }


    class Model(val validator: Validator) {
        internal val nameDoc = PlainDocument()
        internal val extensionDoc = PlainDocument()
        internal val buildCommandDoc = PlainDocument()

        // ui shouldn't be here
        internal val nameErrorComponent = ErrorComponent()
        internal val extensionErrorComponent = ErrorComponent()
        internal val buildCommandErrorComponent = ErrorComponent()

        private var itemRef: Ref<SolutionLanguage?> = Ref()

        fun applyItem(item: SolutionLanguage?) {
            // replace text fields
            nameDoc.replace(0, nameDoc.length, item?.name ?: "", null)
            extensionDoc.replace(0, extensionDoc.length, item?.extension ?: "", null)
            buildCommandDoc.replace(0, buildCommandDoc.length, item?.buildCommand ?: "", null)

            setCorrespondingItem(item)
        }

        fun createItemValue(): SolutionLanguage? {
            val item = itemRef.get() ?: return null

            var name = nameDoc.getText(0, nameDoc.length)
            var extension = extensionDoc.getText(0, extensionDoc.length)
            var buildCommand = buildCommandDoc.getText(0, buildCommandDoc.length)

            val nameError = if (item.name != name) validator.validateName(name) else null
            val extensionError = validator.validateExtension(extension)
            val buildCommandError = validator.validateBuildCommand(buildCommand)


            nameErrorComponent.setErrorMessage(nameError)
            extensionErrorComponent.setErrorMessage(extensionError)
            buildCommandErrorComponent.setErrorMessage(buildCommandError)

            // not updating invalid fields
            if (nameError != null)
                name = item.name
            if (extensionError != null)
                extension = item.extension
            if (buildCommandError != null)
                buildCommand = item.buildCommand

            return SolutionLanguage(name, extension, buildCommand)
        }

        fun setCorrespondingItem(item: SolutionLanguage?) = this.itemRef.set(item)

        fun getCorrespondingItem(): SolutionLanguage? = this.itemRef.get()
    }

    interface Validator {
        fun validateName(name: String): String?
        fun validateExtension(extension: String): String?
        fun validateBuildCommand(buildCommand: String): String?
    }
}