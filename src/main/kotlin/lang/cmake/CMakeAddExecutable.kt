package lang.cmake

import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.application.runUndoTransparentWriteAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import gather.FileGenerationListener
import settings.projectSettings.cmake.cmakeSettings
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.relativeTo


class CMakeAddExecutable(val project: Project) : FileGenerationListener {

    val cmakePath = Paths.get(project.basePath!!, "CMakeLists.txt").toFile()
    val settings = project.cmakeSettings()

    override fun onGenerated(file: VirtualFile) {

        if (!settings.addToCMakeLists) return

        invokeLater(ModalityState.NON_MODAL) {

            val cmakeFile = LocalFileSystem.getInstance().findFileByIoFile(cmakePath)
                ?: return@invokeLater
            val cmakeDoc = runReadAction { FileDocumentManager.getInstance().getDocument(cmakeFile) }
                ?: return@invokeLater

            val cmakeText = cmakeDoc.text
            val relativePath = file.pathRelativeToProject()
            val name = file.nameWithoutExtension

            val regex = "add_executable\\s*\\(\\s*(\\S+)(?:\\s+(?:\"([^\"]+)|([^ \"]+))\\s*)?".toRegex()
            val matches = regex.findAll(cmakeText)

            matches.forEachIndexed { index, match ->
                if (match.groupValues[1] == name) {
                    runUndoTransparentWriteAction {
                        val endIndex = matches.elementAt(index).groups[1]!!.range.last + 1
                        cmakeDoc.insertString(endIndex, " \"${relativePath}\"")
                    }
                    return@invokeLater
                }
            }
            runUndoTransparentWriteAction {
                cmakeDoc.insertString(cmakeText.length, "\nadd_executable($name \"${relativePath}\")")
            }
        }
    }

    private fun VirtualFile.pathRelativeToProject(): String {
        return Path(path).relativeTo(Path(project.basePath!!)).invariantSeparatorsPathString
    }
}