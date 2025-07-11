package com.github.pushpavel.autocp.database.models

import com.github.pushpavel.autocp.database.autoCp
import com.intellij.openapi.project.Project
import kotlinx.serialization.Serializable

@Serializable
data class SolutionFile(
    val pathString: String,
    val linkedProblemId: Pair<String, String>?,
    val testcases: List<Testcase>,
    val generator: Generator = Generator(),
    val judgeSettings: JudgeSettings = JudgeSettings(),
) {

    fun getLinkedProblem(project: Project): Problem? {
        if (linkedProblemId == null) return null
        return project.autoCp().problems[linkedProblemId.first]?.get(linkedProblemId.second)
    }
}

@Serializable
data class Generator (
    val generatorProgram: Program = Program(),
    val stressTestcaseAmount: Int = 1000,

    val correctProgram: Program = Program(),

    val useStaticTestcases: Boolean = false,
)

@Serializable
data class JudgeSettings (
    val memoryLimit: Long = 256,
    val timeLimit: Long = 1000,
    val inputFile: String? = null,
    val outputFile: String? = null,

    val judgeProgram: Program = Program(),
    val isInteractive: Boolean = false,
    val preferJudgeOverOutput: Boolean = true,
)

@Serializable
data class Program (
    val code: String? = null,
    val languageExtension: String = "cpp"
)
