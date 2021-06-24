package tester.execute

import com.intellij.openapi.Disposable

@Deprecated("use TestingProcess")
interface ProcessLike : Disposable {
    fun start()
}