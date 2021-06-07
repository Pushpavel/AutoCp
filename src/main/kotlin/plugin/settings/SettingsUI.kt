package plugin.settings

import com.intellij.ui.JBSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.layout.panel
import java.awt.BorderLayout

class SettingsUI : JBPanel<SettingsUI>(BorderLayout()) {

    val model = SettingsModel()

    private val mainPanelContent by lazy { LanguagePanelUI(model.languagePanelModel) }

    private val mainPanel = JBPanelWithEmptyText().withEmptyText("Create a New Solution Language")

    private val sideList = JBList(model)
        .also {
            it.selectionModel = model.sideListSelectionModel
            it.addListSelectionListener { _ ->
                // model.languagePanelModel.setCorrespondingItem() is not yet called by SettingsModel
                // this is because list selection listeners are called in the opposite order they are added
                if ((model.languagePanelModel.getCorrespondingItem() == null) == (it.selectedIndex == -1))
                    return@addListSelectionListener

                // removing mainPanelContent
                mainPanel.removeAll()
                if (it.selectedIndex != -1)
                    mainPanel.add(mainPanelContent.component)

                // updates ui
                mainPanel.revalidate()
                mainPanel.repaint()
            }
        }
        .let {
            ToolbarDecorator
                .createDecorator(it)
                .setAddAction {
                    model.duplicateOrAddItem()
                }
                .createPanel()
        }


    init {
        add(panel {
            row("Preferred Language:") {
                comboBox(model, { null }, {})
            }
        }, BorderLayout.NORTH)

        add(JBSplitter(0.3F).apply {
            firstComponent = sideList
            secondComponent = mainPanel
        }, BorderLayout.CENTER)

    }

}