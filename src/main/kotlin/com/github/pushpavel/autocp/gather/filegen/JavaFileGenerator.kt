package com.github.pushpavel.autocp.gather.filegen

import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.gather.models.FileGenerationDto
import com.github.pushpavel.autocp.gather.models.GenerateFileErr
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.rootManager
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import org.jetbrains.jps.model.java.JavaSourceRootType
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.pathString

class JavaFileGenerator(project: Project) : DefaultFileGenerator(project) {
    override fun isSupported(extension: String) = extension == "java"

    override fun getValidFileName(fileName: String): String {
        return convertFileNameAndValidate(defaultConversion, fileName)
    }

    override fun getParentPsiDir(rootPsiDir: PsiDirectory, problem: Problem, extension: String): PsiDirectory {
        val packageName = getValidFileName(problem.name)
        val parentPath = Paths.get(rootPsiDir.virtualFile.path, packageName)
        val parentDir = VfsUtil.createDirectories(parentPath.pathString)
        return runReadAction { PsiManager.getInstance(project).findDirectory(parentDir)!! }
    }

    override fun getFileNameWithExtension(parentPsiDir: PsiDirectory, fileName: String, extension: String): String {
        return "Main.java"
    }

    override fun generateFile(extension: String, dto: FileGenerationDto, problem: Problem, batch: BatchJson): VirtualFile? {
        var file: VirtualFile? = null

        try {
            file = super.generateFile(extension, dto, problem, batch)
        } catch (e: GenerateFileErr.FileAlreadyExistsErr) {
            file = VfsUtil.findFile(Path(e.filePath), true)
            throw e
        } finally {
            if (file != null)
                runWriteAction {
                    val parent = file.parent
                    val m = ModuleUtil.findModuleForFile(parent, project)
                    if (m != null) {
                        val f = ProjectRootManager.getInstance(project).fileIndex.getContentRootForFile(parent)
                        val model = m.rootManager.modifiableModel
                        model.contentEntries.firstOrNull { c -> c.file == f }?.addExcludeFolder(parent)
                        model.commit()
                    }
                    if (ModuleManager.getInstance(project).findModuleByName(parent.name) == null) {
                        val module = ModuleManager.getInstance(project)
                            .newModule(
                                Paths.get(parent.pathString, "${defaultConversion(parent.name)}.iml"),
                                "JAVA_MODULE"
                            )
                        val model = module.rootManager.modifiableModel
                        val contentEntry = model.addContentEntry(parent)
                        contentEntry.addSourceFolder(parent, JavaSourceRootType.SOURCE)
                        model.inheritSdk()
                        model.commit()
                    }
                }

        }
        return file
    }
}