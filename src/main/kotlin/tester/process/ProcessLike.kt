package tester.process

import com.intellij.openapi.Disposable

interface ProcessLike : Disposable {
    fun start()
}