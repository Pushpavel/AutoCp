package plugin.settings

import com.intellij.ui.layout.panel
import javax.swing.JTextField

class AutoCpSettingsUI {

    lateinit var text: JTextField

    val component = panel {
        row {
            textField({ "" }, {},3).also {
                text = it.component
            }
        }
    }
}