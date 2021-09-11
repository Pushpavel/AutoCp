package com.github.pushpavel.autocp.database

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.LocalFileSystem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Paths
import kotlin.io.path.Path


@Service
class AutoCpStorage(project: Project) {

    val database by lazy {
        val path = Paths.get(project.basePath!!, ".autocp")
        val virtualFile = LocalFileSystem.getInstance().findFileByNioFile(path)

        val db = if (virtualFile?.isValid == true) {
            runReadAction {
                val document = FileDocumentManager.getInstance().getDocument(virtualFile)!!
                // TODO: show error notification for document null due to file association changes
                Json.decodeFromString(document.text)
                // TODO: show error notification for .autocp in invalid format
            }
        } else
            AutoCpDB()

        AutoCpDatabase(MutableStateFlow(db.problems), MutableStateFlow(db.solutionFiles))
    }

    val serializableDatabase get() = database.run { AutoCpDB(problems, solutionFiles) }

}

class AutoCpStorageSaver : FileDocumentManagerListener {

    override fun beforeAllDocumentsSaving() {
        ProjectManager.getInstanceIfCreated()?.openProjects?.forEach { project ->

            val path = Paths.get(project.basePath!!, ".autocp")
            var virtualFile = LocalFileSystem.getInstance().findFileByNioFile(path)
            val db = project.service<AutoCpStorage>().serializableDatabase


            if (virtualFile?.isValid != true) {
                if (DEFAULT_AUTO_CP_DB != db) {
                    val projectRoot =
                        LocalFileSystem.getInstance().findFileByNioFile(Path(project.basePath!!)) ?: return

                    runWriteAction {
                        virtualFile = projectRoot.createChildData(FileDocumentManager.getInstance(), ".autocp")
                    }
                }
            }


            if (virtualFile?.isValid != true)
                return

            runReadAction {
                val document = FileDocumentManager.getInstance().getDocument(virtualFile!!)

                //TODO: check if document is null and warn user about file associations change for .autocp

                runWriteAction {
                    document?.setText(Json.encodeToString(db))
                }
            }
        }
    }
}


fun Project.autoCp(): AutoCpDatabase = service<AutoCpStorage>().database

val DEFAULT_AUTO_CP_DB = AutoCpDB()
