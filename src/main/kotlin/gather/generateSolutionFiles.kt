package gather

import com.github.pushpavel.autocp.database.Problem
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.util.io.exists
import database.AcpDatabase
import settings.langSettings.model.Lang
import java.nio.file.Paths
import kotlin.io.path.pathString

/**
 * creates files and associates those files with a problem in the db
 */
fun generateSolutionFiles(project: Project, problems: List<Problem>, lang: Lang) {
    if (problems.isEmpty()) return
    val rootDir = Paths.get(project.basePath!!, problems[0].groupName).toFile()
    if (!rootDir.exists())
        rootDir.mkdir()

    val service = project.service<AcpDatabase>()

    problems.forEach {
        // TODO: generate from file Templates
        val solutionPath = Paths.get(rootDir.path, "${it.name}.cpp")

        if (!solutionPath.exists())
            solutionPath.toFile().createNewFile()

        service.associateSolutionToProblem(solutionPath.pathString, it).getOrThrow()
    }

    ProjectView.getInstance(project).refresh()
}