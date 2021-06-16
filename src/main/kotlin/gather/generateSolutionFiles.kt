package gather

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.util.io.exists
import database.AcpDatabase
import dev.pushpavel.autocp.database.Problem
import plugin.settings.SolutionLanguage
import java.nio.file.Paths
import kotlin.io.path.pathString

fun generateSolutionFiles(project: Project, problems: List<Problem>, lang: SolutionLanguage) {
    if (problems.isEmpty()) return
    val rootDir = Paths.get(project.basePath!!, problems[0].groupName).toFile()
    if (!rootDir.exists())
        rootDir.mkdir()

    val service = project.service<AcpDatabase>()

    problems.forEach {
        val solutionPath = Paths.get(rootDir.path, "${it.name}.${lang.extension}")

        if (!solutionPath.exists())
            solutionPath.toFile().createNewFile()

        service.associateSolutionToProblem(solutionPath.pathString, it).getOrThrow()
    }
}