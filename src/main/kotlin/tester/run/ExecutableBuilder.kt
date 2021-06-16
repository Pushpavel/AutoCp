package tester.run

import com.intellij.execution.configurations.GeneralCommandLine
import dev.pushpavel.autocp.database.Problem
import config.AutoCpConfig
import settings.AutoCpSettings
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.pathString

class ExecutableBuilder(private val problem: Problem, private val config: AutoCpConfig) : ProgramExecutor("") {

    lateinit var outputPath: String
    val commandLine = run {
        val settings = AutoCpSettings.instance
        val lang = settings.getLangWithId(config.solutionLangId)
            ?: throw IllegalStateException("Select Solution Language in Run Configuration \"${config.name}\"")
        val tempDir = Files.createTempDirectory("AutoCp-" + System.currentTimeMillis())
        if (!tempDir.exists())
            tempDir.toFile().mkdir()
        val outputPath =
            Paths.get(tempDir.pathString, "AutoCp-" + problem.name + "-" + System.currentTimeMillis() + ".exe")
        this.outputPath = outputPath.pathString
        val command = lang.buildCommandString(config.solutionFilePath, outputPath.pathString)
        val commandList = Regex(""""(\\"|[^"])*?"|[^\s]+""").findAll(command).toList()
        GeneralCommandLine(commandList.map { it.value.trim('"') })
    }


    override fun createProcess(): Process {
        return commandLine.createProcess()
    }

}