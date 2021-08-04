package database

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project


@State(
    name = "autoCp",
    storages = [Storage("autocp.xml")],
    defaultStateAsResource = true
)
@Service
class AutoCpStorage(project: Project) : PersistentStateComponent<AutoCpDB> {

    lateinit var database: AutoCpDB

    override fun getState(): AutoCpDB {
        return database
    }

    override fun loadState(state: AutoCpDB) {
        database = state
    }
}

fun Project.autoCp(): AutoCpDB = service<AutoCpStorage>().database