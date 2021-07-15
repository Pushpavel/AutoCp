package settings.langSettings.ui.ideLangSelector

import com.intellij.lang.Language
import com.intellij.openapi.roots.ui.componentsList.components.ScrollablePanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.ListSpeedSearch
import com.intellij.ui.SpeedSearchBase
import com.intellij.ui.SpeedSearchComparator
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.speedSearch.SpeedSearch
import com.intellij.ui.speedSearch.SpeedSearchSupply
import com.intellij.ui.speedSearch.SpeedSearchUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ui.vvm.swingModels.toSingleSelectionModel
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel


class IDELangSelectorDialog : DialogWrapper(false) {

    private val languages = Language.getRegisteredLanguages()
    private val selectedIndex = MutableStateFlow(-1)
    private val scope = MainScope()

    init {
        title = "Select Programming Language"
        init()
        scope.launch {
            selectedIndex.collect {
                isOKActionEnabled = it != -1
            }
        }
    }

    override fun createCenterPanel(): JComponent {
        val dialogPanel = JPanel(BorderLayout())

        val list = JBList(languages)
        list.selectionModel = selectedIndex.toSingleSelectionModel(scope)
        ListSpeedSearch(list) { it.displayName }

        val container = JBScrollPane(list)

        dialogPanel.add(container, BorderLayout.CENTER)
        return dialogPanel
    }

    fun showAndGetSelection(): Language? {
        val confirm = showAndGet()

        if (confirm && selectedIndex.value != -1)
            return languages.elementAt(selectedIndex.value)

        return null
    }

    override fun dispose() {
        super.dispose()
        scope.cancel()
    }
}