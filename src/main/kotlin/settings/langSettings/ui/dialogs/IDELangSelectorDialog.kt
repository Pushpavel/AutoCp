package settings.langSettings.ui.dialogs

import com.intellij.lang.Language
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.ListSpeedSearch
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import settings.langSettings.model.Lang
import ui.StringCellRenderer
import ui.helpers.mainScope
import ui.vvm.swingModels.toSingleSelectionModel
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel


class IDELangSelectorDialog(addedLangList: List<Lang>) : DialogWrapper(false) {

    private val languages = Language.getRegisteredLanguages().run {
        val addedLangMap = addedLangList.associateBy { it.langId }
        filter {
            it.associatedFileType?.icon != null && !addedLangMap.containsKey(it.id)
        }
    }
    private val selectedIndex = MutableStateFlow(-1)
    private val scope = mainScope()

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
        list.cellRenderer = StringCellRenderer<Language> {
            Pair(it.displayName, it.associatedFileType?.icon)
        }

        list.selectionModel = selectedIndex.toSingleSelectionModel(scope, selectedIndex)
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