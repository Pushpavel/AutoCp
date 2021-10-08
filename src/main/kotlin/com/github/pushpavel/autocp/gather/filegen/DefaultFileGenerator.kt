package com.github.pushpavel.autocp.gather.filegen

import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.FileTemplates
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.gather.models.GenerateFileErr
import com.github.pushpavel.autocp.settings.generalSettings.AutoCpGeneralSettings
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import com.intellij.util.IncorrectOperationException
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.pathString

open class DefaultFileGenerator(val project: Project) : FileGenerator {

    val generalSettings = AutoCpGeneralSettings.instance

    override fun isSupported(extension: String) = true

    open fun getRootPsiDir(groupName: String): PsiDirectory {
        val relPath = generalSettings.constructFileGenerationRoot(groupName).pathString
        val rootPath = Paths.get(Path(project.basePath!!).pathString, relPath)
        val rootDir = VfsUtil.createDirectories(rootPath.pathString)
        return runReadAction { PsiManager.getInstance(project).findDirectory(rootDir)!! }
    }

    open fun getFileTemplate(extension: String): FileTemplate {
        return FileTemplates.cpTemplateForExtension(extension, project)
    }

    open fun getParentPsiDir(rootPsiDir: PsiDirectory, problem: Problem, extension: String) = rootPsiDir

    open fun getFileNameWithExtension(parentPsiDir: PsiDirectory, problem: Problem, extension: String): String {
        val fileName = problem.name
            .replace(' ', '_')
            .replace('-', '_')
            .replace("[^0-9a-zA-Z_]".toRegex(), "")

        return "$fileName.$extension"
    }

    override fun generateFile(extension: String, problem: Problem, batch: BatchJson): VirtualFile? {
        val rootPsiDir = getRootPsiDir(problem.groupName)
        val fileTemplate = getFileTemplate(extension)
        val parentPsiDir = getParentPsiDir(rootPsiDir, problem, extension)
        val fileName = getFileNameWithExtension(parentPsiDir, problem, extension)

        try {
            parentPsiDir.checkCreateFile(fileName)
        } catch (e: IncorrectOperationException) {
            throw GenerateFileErr.FileAlreadyExistsErr(
                Paths.get(
                    parentPsiDir.virtualFile.pathString,
                    fileName
                ).pathString, problem
            )
        }

        val psiFile = CreateFileFromTemplateAction.createFileFromTemplate(
            fileName,
            fileTemplate,
            parentPsiDir,
            null,
            false
        )

        return psiFile?.virtualFile
    }
}