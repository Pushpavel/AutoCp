package settings.langSettings.ui.langItem

import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanel
import settings.langSettings.model.BuildProperties
import java.awt.BorderLayout

class LangItemView : JBPanel<LangItemView>(BorderLayout()) {

    init {

        val list = JBList<BuildProperties>()
        val container = ToolbarDecorator.createDecorator(list).createPanel()

        add(container, BorderLayout.CENTER)
    }

}