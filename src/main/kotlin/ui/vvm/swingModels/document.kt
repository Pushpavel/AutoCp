package ui.vvm.swingModels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ui.helpers.getText
import ui.helpers.onChange
import ui.helpers.setText
import javax.swing.text.PlainDocument

fun MutableSharedFlow<String>.toPlainDocument(scope: CoroutineScope): PlainDocument {
    return this.toPlainDocument(scope, this)
}

fun Flow<String>.toPlainDocument(
    scope: CoroutineScope,
    sink: MutableSharedFlow<String>
): PlainDocument {
    val doc = PlainDocument()

    // flow to doc
    scope.launch {
        collect {
            doc.setText(it)
        }
    }

    // doc to sink
    doc.onChange {
        scope.launch {
            sink.emit(doc.getText())
        }
    }

    return doc
}
