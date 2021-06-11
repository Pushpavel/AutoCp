package common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun runUI(runnable: Runnable) {
    GlobalScope.launch(Dispatchers.Main) {
        runnable.run()
    }
}

