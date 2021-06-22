package tester.execute

import com.intellij.openapi.Disposable

interface ProcessLike : Disposable {
    fun start()
}