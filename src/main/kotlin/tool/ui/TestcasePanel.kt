package tool.ui

import com.intellij.ide.ui.fullRow
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import database.models.Testcase
import ui.swing.editableList.ListItemView

class TestcasePanel : ListItemView<Testcase> {

    override val component = panel(LCFlags.fillX) {
        blockRow { }
        fullRow {
            label("Something")
        }
        blockRow { }
    }


    override fun contentChanged(item: Testcase) {

    }

    override fun dispose() {

    }

}