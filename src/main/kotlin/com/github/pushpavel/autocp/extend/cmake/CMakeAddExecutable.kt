package com.github.pushpavel.autocp.extend.cmake

import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.filegen.FileGenerationListener
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.gather.models.GenerateFileErr
import com.github.pushpavel.autocp.settings.projectSettings.cmake.cmakeSettings
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.application.runUndoTransparentWriteAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.relativeTo


class CMakeAddExecutable(val project: Project) : FileGenerationListener {

    val cmakePath = Paths.get(project.basePath!!, "CMakeLists.txt")
    val settings = project.cmakeSettings()

    override fun onGenerated(file: VirtualFile, problem: Problem, batch: BatchJson, extension: String) {
        addToCmake(file)
    }

    override fun onError(e: Exception, problem: Problem, batch: BatchJson, extension: String) {
        if (e is GenerateFileErr.FileAlreadyExistsErr) {
            val file = VfsUtil.findFile(Path(e.filePath), true)
            if (file != null)
                addToCmake(file)
        }
    }

    private fun addToCmake(file: VirtualFile) {

        if (!settings.addToCMakeLists) return

        invokeLater(ModalityState.nonModal()) {

            val cmakeFile = VfsUtil.findFile(cmakePath, true)
                ?: return@invokeLater
            val cmakeDoc = runReadAction { FileDocumentManager.getInstance().getDocument(cmakeFile) }
                ?: return@invokeLater

            val cmakeText = cmakeDoc.text
            val relativePath = file.pathRelativeToProject()
            val name = file.nameWithoutExtension

            val regex = "add_executable\\s*\\(\\s*(\\S+)(?:\\s+(?:\"([^\"]+)|([^\\s)\"]+))\\s*)?".toRegex()
            val matches = regex.findAll(cmakeText)

            matches.forEachIndexed { index, match ->
                if (match.groupValues[1] == name) {
                    if (match.groupValues[2] == relativePath)
                        return@invokeLater

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