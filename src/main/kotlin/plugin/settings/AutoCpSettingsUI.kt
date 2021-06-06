package plugin.settings

import com.intellij.ide.macro.MacrosDialog
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.*
import files.editor.ui.UIComponent
import plugin.ui.SolutionLanguagesListModel
import java.awt.GridLayout

class AutoCpSettingsUI {

    val comboBoxModel = CollectionComboBoxModel(mutableListOf<String>(), null)
    lateinit var listModel: SolutionLanguagesListModel

    private lateinit var solutionLanguageWrapper: SolutionLanguageWrapper


    val component = panel(LCFlags.fillX) {
        row("Preferred Solution Language") {
            comboBox(comboBoxModel, { null }, {})
        }
        row {
            OnePixelSplitter(false, 0.2F).apply {
                firstComponent = JBList<SolutionLanguage>().let {
                    // creating list model
                    listModel = SolutionLanguagesListModel(it).also { model -> it.model = model }

                    // on selection change
                    it.addListSelectionListener { e ->
                        if (e.valueIsAdjusting) return@addListSelectionListener
                        // refresh solutionLanguageComponent
                        solutionLanguageWrapper.receiveState(listModel.getSelection())
                    }

                    ToolbarDecorator
                        .createDecorator(it)
                        .setAddAction {

                        }.setRemoveAction {

                        }.createPanel()
                }

                secondComponent = SolutionLanguageWrapper(null) { state ->
                    listModel.getSelection()?.let {
                        it.name = state?.name ?: ""
                        it.extension = state?.extension ?: ""
                        it.buildCommand = state?.buildCommand ?: ""
                    }
                }.also {
                    solutionLanguageWrapper = it
                }.component

            }(grow, pushY)
        }
    }


    private class SolutionLanguageWrapper(
        initState: SolutionLanguage?,
        onComponentUpdate: ((SolutionLanguage?) -> Unit)?
    ) : UIComponent<JBPanelWithEmptyText, SolutionLanguage?>(initState, onComponentUpdate) {
        override val component = JBPanelWithEmptyText(GridLayout()).withEmptyText("Create a solution language")

        private val nameField = ExtendableTextField(10)
        private val extensionField = ExtendableTextField(1)
        private val buildCommandField = ExtendableTextField()

        private val solutionLanguageComponent = panel(LCFlags.fillX) {
            MacrosDialog.addTextFieldExtension(buildCommandField)

            row {
                row("Name:") { nameField() }
                row("Extension:") { extensionField() }.largeGapAfter()
                row("Build Command:") { buildCommandField(growX) }
            }
        }

        init {
            nameField.onChange {
                state?.apply {
                    name = nameField.text
                }
                setState()
            }
            extensionField.onChange {
                state?.apply {
                    extension = extensionField.text
                }
                setState()
            }
            buildCommandField.onChange {
                state?.apply {
                    buildCommand = buildCommandField.text
                }
                setState()
            }
        }

        override fun onStateUpdate(state: SolutionLanguage?) {
            component.removeAll()

            state?.let {
                component.add(solutionLanguageComponent)
                nameField.text = it.name
                extensionField.text = it.extension
                buildCommandField.text = it.buildCommand
            }

            markDirty()
        }
    }
}