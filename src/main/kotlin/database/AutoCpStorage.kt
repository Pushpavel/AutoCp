package database

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFileManager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Paths
import kotlin.io.path.Path


@Service
class AutoCpStorage(project: Project) {

    val database by lazy {
        val path = Paths.get(project.basePath!!, ".autocp")
        val virtualFile = VirtualFileManager.getInstance().findFileByNioPath(path)

        if (virtualFile?.isValid == true) {
            runReadAction {
                val document = FileDocumentManager.getInstance().getDocument(virtualFile)!!
                Json.decodeFromString(document.text)
            }
        } else
            AutoCpDB()
    }

}

class AutoCpStorageSaver : FileDocumentManagerListener {

    var database = AutoCpDB()
    override fun beforeAllDocumentsSaving() {
        ProjectManager.getInstanceIfCreated()?.openProjects?.forEach { project ->

            val path = Paths.get(project.basePath!!, ".autocp")
            var virtualFile = VirtualFileManager.getInstance().findFileByNioPath(path)
            val db = project.service<AutoCpStorage>().database


            if (virtualFile?.isValid != true) {
                if (DEFAULT_AUTO_CP_DB != db) {
                    val projectRoot =
                        VirtualFileManager.getInstance().findFileByNioPath(Path(project.basePath!!)) ?: return

                    runWriteAction {
                        virtualFile = projectRoot.createChildData(null, ".autocp")
                    }
                }
            }


            if (virtualFile?.isValid != true)
                return

            runReadAction {
                val document = FileDocumentManager.getInstance().getDocument(virtualFile!!)
                println("read Document ${document != null}")
                runWriteAction {
                    document?.setText(Json.encodeToString(db))
                }
            }
        }
    }
}


fun Project.autoCp(): AutoCpDB = service<AutoCpStorage>().database

val DEFAULT_AUTO_CP_DB = AutoCpDB()
