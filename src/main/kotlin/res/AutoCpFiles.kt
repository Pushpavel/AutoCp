package res

import com.intellij.util.io.readText
import common.helpers.listPathsInDirectoryInResources

object AutoCpFiles {
    val langJsons = listPathsInDirectoryInResources("/languages").map { it.readText() }
}