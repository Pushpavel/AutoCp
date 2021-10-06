package com.github.pushpavel.autocp.gather.filegen

import com.github.pushpavel.autocp.common.helpers.constructFileNameWithExt
import com.github.pushpavel.autocp.common.helpers.ioScope
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.FileTemplates
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.gather.models.GenerateFileErr
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import com.intellij.util.IncorrectOperationException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name
import kotlin.io.path.pathString

val scope = ioScope()

class DefaultFileGenerator(val project: Project) : FileGenerator {

    override fun isSupported(extension: String) = true

    protected fun getRootPsiDir(groupName: String): PsiDirectory {
        val rootPath = Paths.get(project.basePath!!, groupName)
        val rootDir = VfsUtil.createDirectories(rootPath.pathString)
        return runReadAction { PsiManager.getInstance(project).findDirectory(rootDir)!! }
    }

    protected fun getFileTemplate(extension: String): FileTemplate {
        return FileTemplates.cpTemplateForExtension(extension, project)
    }

    protected fun getFilePath(rootPsiDir: PsiDirectory, problem: Problem, fileTemplate: FileTemplate): Path {
        val fileName = fileTemplate.constructFileNameWithExt(
            problem.name
                .replace(' ', '_')
                .replace('-', '_')
                .replace("[^0-9a-zA-Z_]".toRegex(), "")
        )
        return Paths.get(rootPsiDir.virtualFile.path, fileName)
    }

    override fun generateFile(extension: String, problem: Problem, batch: BatchJson): VirtualFile? {
        val rootPsiDir = getRootPsiDir(problem.groupName)
        val fileTemplate = getFileTemplate(extension)
        val filePath = getFilePath(rootPsiDir, problem, fileTemplate)

        try {
            rootPsiDir.checkCreateFile(filePath.name)
        } catch (e: IncorrectOperationException) {
            throw GenerateFileErr.FileAlreadyExistsErr(filePath.pathString, problem)
        }

        val psiFile = CreateFileFromTemplateAction.createFileFromTemplate(
            filePath.name,
            fileTemplate,
            rootPsiDir,
            null,
            false
        )

        return psiFile?.virtualFile
    }
}