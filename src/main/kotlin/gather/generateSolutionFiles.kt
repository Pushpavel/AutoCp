package gather

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.OpenFileAction
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiManager
import database.autoCp
import database.models.Problem
import settings.langSettings.model.Lang
import java.nio.file.Paths
import kotlin.io.path.pathString

/**
 * creates files and associates those files with a problem in the db
 */
fun generateSolutionFiles(project: Project, problems: List<Problem>, lang: Lang) {
    if (problems.isEmpty()) return
    val rootPath = Paths.get(project.basePath!!, problems[0].groupName)
    val rootDir = VfsUtil.createDirectories(rootPath.pathString)
    val rootPsiDir = PsiManager.getInstance(project).findDirectory(rootDir)!!

    problems.forEach {

        val file = CreateFileFromTemplateAction.createFileFromTemplate(
            it.name,
            // TODO: catch no default file Template case
            lang.getFileTemplate()!!,
            rootPsiDir,
            null,
            false
        )!!

        project.autoCp().createSolutionFile(file.virtualFile.path, Pair(it.groupName, it.name))

        OpenFileAction.openFile(file.virtualFile, project)
    }

    ProjectView.getInstance(project).refresh()
}