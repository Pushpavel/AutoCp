package com.github.pushpavel.autocp.gather.filegen

import com.github.pushpavel.autocp.common.helpers.constructFileNameWithExt
import com.github.pushpavel.autocp.common.helpers.ioScope
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.FileTemplates
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.gather.models.GenerateFileErr
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.util.IncorrectOperationException
import java.nio.file.Paths
import kotlin.io.path.pathString

val scope = ioScope()

class DefaultFileGenerator(val project: Project) : FileGenerator {

    override fun isSupported(extension: String) = true

    override fun generateFile(extension: String, problem: Problem, batch: BatchJson): VirtualFile? {
        val rootPath = Paths.get(project.basePath!!, problem.groupName)
        val rootDir = VfsUtil.createDirectories(rootPath.pathString)
        val rootPsiDir = runReadAction { PsiManager.getInstance(project).findDirectory(rootDir)!! }
        val fileTemplate = FileTemplates.cpTemplateForExtension(extension, project)
        val fileName = fileTemplate.constructFileNameWithExt(
            problem.name
                .replace(' ', '_')
                .replace('-', '_')
                .replace("[^0-9a-zA-Z_]".toRegex(), "")
        )

        val filePath = Paths.get(rootPsiDir.virtualFile.path, fileName).pathString

        try {
            rootPsiDir.checkCreateFile(fileName)
        } catch (e: IncorrectOperationException) {
            throw GenerateFileErr.FileAlreadyExistsErr(filePath, problem)
        }

        val psiFile = CreateFileFromTemplateAction.createFileFromTemplate(
            fileName,
            fileTemplate,
            rootPsiDir,
            null,
            false
        )
        ProjectView.getInstance(project).refresh()

        return psiFile?.virtualFile
    }
}