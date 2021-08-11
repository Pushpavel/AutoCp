package settings.langSettings.ui.dialogs

import com.intellij.lang.Language
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.ListSpeedSearch
import com.intellij.ui.components.JBList
import com.intellij.ui.layout.panel
import common.ui.helpers.onSelectedValue
import common.ui.swing.TileCellRenderer
import settings.langSettings.model.Lang


class IDELangSelectorDialog(addedLangList: List<Lang>) : DialogWrapper(false) {

    private val languages = Language.getRegisteredLanguages().run {
        val addedLangMap = addedLangList.associateBy { it.langId }
        filter {
            it.associatedFileType?.icon != null && !addedLangMap.containsKey(it.id)
        }
    }

    val list = JBList(languages)


    init {
        title = "Select Programming Language"
        init()
        list.onSelectedValue {
            isOKActionEnabled = selectedIndex != -1
        }
    }

    override fun createCenterPanel() = panel {

        list.cellRenderer = TileCellRenderer {
            text = it.displayName
            icon = it.associatedFileType?.icon
        }

        ListSpeedSearch(list) { it.displayName }
        row {
            scrollPane(list)
        }
    }

    fun showAndGetSelection(): Language? {
        val confirm = showAndGet()

        if (confirm && list.selectedIndex != -1)
            return list.selectedValue

        return null
    }
}