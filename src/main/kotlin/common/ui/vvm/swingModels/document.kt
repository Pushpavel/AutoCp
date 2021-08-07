package common.ui.vvm.swingModels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import common.ui.helpers.getText
import common.ui.helpers.onChange
import common.ui.helpers.setText
import javax.swing.text.PlainDocument

fun CoroutineScope.plainDocument(flow: MutableSharedFlow<String>): PlainDocument {
    return plainDocument(flow, flow)
}

fun CoroutineScope.plainDocument(source: Flow<String>, sink: MutableSharedFlow<String>): PlainDocument {
    val doc = PlainDocument()

    // flow to doc
    launch {
        source.collect {
            if (doc.getText() != it)
                doc.setText(it)
        }
    }

    // doc to sink
    doc.onChange {
        launch {
            sink.emit(doc.getText())
        }
    }

    return doc
}