package com.github.pushpavel.autocp.database

import com.github.pushpavel.autocp.common.compat.base.AutoCpFileConversion
import com.github.pushpavel.autocp.common.res.R
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.readText


@Service(Service.Level.PROJECT)
class AutoCpStorage(val project: Project) {

    val database by lazy {
        if(!project.isDefault){
            val converter = AutoCpFileConversion(project)
            converter.convert()
        }

        val path = Paths.get(if (project.isDefault)  "notexists" else project.basePath!!, ".autocp")

        val db = if (path.exists()) {
            runReadAction { Json.decodeFromString(path.readText()) }
        } else
            AutoCpDB(R.keys.autoCpFileVersionNumber)

        AutoCpDatabase(MutableStateFlow(db.problems), MutableStateFlow(db.solutionFiles))
    }

    val serializableDatabase
        get() = database.run {
            AutoCpDB(
                R.keys.autoCpFileVersionNumber,
                problems,
                solutionFilesFlow.value
            )
        }

}

class AutoCpStorageSaver : FileDocumentManagerListener {

    override fun beforeAllDocumentsSaving() {
        ProjectManager.getInstanceIfCreated()?.openProjects?.forEach { project ->
            if(project.isDefault)
                return
            val path = Paths.get(Path(project.basePath!!).pathString, ".autocp")
            var virtualFile = VfsUtil.findFile(path, true)
            val db = project.service<AutoCpStorage>().serializableDatabase


            if (virtualFile?.isValid != true && DEFAULT_AUTO_CP_DB != db) {
                val projectRoot = VfsUtil.findFile(Path(project.basePath!!), true) ?: return

                runWriteAction {
                    val psiDir = PsiManager.getInstance(project).findDirectory(projectRoot) ?: return@runWriteAction
                    virtualFile = psiDir.createFile(".autocp").virtualFile
                }
            }


            if (virtualFile?.isValid != true)
                return

            runReadAction {
                val document = FileDocumentManager.getInstance().getDocument(virtualFile!!)

                if (document == null) {
                    R.notify.couldNotWriteToAutoCpFile()
                    return@runReadAction
                }
                runWriteAction {
                    document.setText(Json.encodeToString(db))
                }
            }
        }
    }
}


fun Project.autoCp(): AutoCpDatabase = service<AutoCpStorage>().database

val DEFAULT_AUTO_CP_DB = AutoCpDB(R.keys.autoCpFileVersionNumber)
