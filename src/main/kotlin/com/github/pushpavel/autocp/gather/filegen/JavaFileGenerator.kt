package com.github.pushpavel.autocp.gather.filegen

import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.models.BatchJson
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
import kotlin.io.path.pathString

class JavaFileGenerator(project: Project) : DefaultFileGenerator(project) {
    override fun isSupported(extension: String) = extension == "java"

    override fun getParentPsiDir(rootPsiDir: PsiDirectory, problem: Problem, extension: String): PsiDirectory {
        val packageName = problem.name
            .replace(' ', '_')
            .replace('-', '_')
            .replace("[^0-9a-zA-Z_]".toRegex(), "")
        val parentPath = Paths.get(rootPsiDir.virtualFile.path, packageName)
        val parentDir = VfsUtil.createDirectories(parentPath.pathString)
        return runReadAction { PsiManager.getInstance(project).findDirectory(parentDir)!! }
    }

    override fun getFileNameWithExtension(parentPsiDir: PsiDirectory, problem: Problem, extension: String): String {
        return "Main.java"
    }

    override fun generateFile(extension: String, problem: Problem, batch: BatchJson): VirtualFile? {
        return super.generateFile(extension, problem, batch)?.also {
            val m = ModuleUtil.findModuleForFile(it.parent, project)
            if (m != null) {
                val f = ProjectRootManager.getInstance(project).fileIndex.getContentRootForFile(it.parent)
                val model = m.rootManager.modifiableModel
                model.contentEntries.firstOrNull { c -> c.file == f }?.addExcludeFolder(it.parent)
                model.commit()
            }
            runWriteAction {
                if (ModuleManager.getInstance(project).findModuleByName(it.parent.name) == null) {
                    val module = ModuleManager.getInstance(project)
                        .newModule(
                            Paths.get(it.parent.pathString, "${it.parent.nameWithoutExtension}.iml"),
                            "JAVA_MODULE"
                        )
                    val model = module.rootManager.modifiableModel
                    val contentEntry = model.addContentEntry(it.parent)
                    contentEntry.addSourceFolder(it.parent, JavaSourceRootType.SOURCE)
                    model.inheritSdk()
                    model.commit()
                    println("inner")
                }
            }
        }
    }
}