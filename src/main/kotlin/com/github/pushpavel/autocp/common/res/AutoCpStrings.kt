@file:Suppress("MemberVisibilityCanBePrivate")

package com.github.pushpavel.autocp.common.res

import com.github.pushpavel.autocp.build.settings.LangNotConfiguredErr
import com.github.pushpavel.autocp.config.validators.SolutionFilePathErr
import com.github.pushpavel.autocp.settings.generalSettings.OpenFileOnGather
import com.github.pushpavel.autocp.tester.base.BuildErr
import com.github.pushpavel.autocp.tester.errors.ProcessRunnerErr
import com.github.pushpavel.autocp.tester.errors.Verdict


object AutoCpStrings {
    const val runConfigName = "AutoCp Solution"
    const val runConfigId = "AutoCp"
    const val runConfigDescription = "Test your Competitive Programming solution with AutoCp"

    const val runStressConfigName = "AutoCp Stress Testing"
    const val runStressConfigId = "AutoCpStress"
    const val runStressConfigDescription = "Test your Competitive Programming solution against many auto-generated input data"
    const val stressConfigNamePrefix = "Stress Testing "

    // File Issue notes
    const val fileIssue = "please file an <a herf=\"https://github.com/Pushpavel/AutoCp/issues/new/choose\">issue</a>"

    fun defaultFileIssue(e: Exception) = "" +
            "An error had occurred. If it is not expected, $fileIssue\n\n" +
            "Error message:\n${e.localizedMessage}\n" +
            "Stacktrace:\n${e.stackTraceToString()}"

    fun fatalFileIssue(e: Exception) = "" +
            "This was not supposed to happen, $fileIssue\n\n" +
            "Error message:\n${e.localizedMessage}\n" +
            "Stacktrace:\n${e.stackTraceToString()}"

    // problemGatheringDialog strings
    const val problemGatheringDialogMsg =
        "You can always change these settings at Settings/Preferences > Tools > AutoCp > Project"

    // Settings strings
    const val projectSettingsOverrideMsg = "Some of these settings could be overridden at Tools > AutoCp > Project"


    // General Settings strings
    const val openFilesOnGatherText = "Open files after generating"

    fun OpenFileOnGather.presentable() = when (this) {
        OpenFileOnGather.NONE -> "None"
        OpenFileOnGather.ONLY_FIRST -> "Only first"
        OpenFileOnGather.ALL -> "All"
    }

    val fileGenerationRootComment = "Relative to project root<br><br>" +
            "Macros:<br>" +
            "<b>${R.keys.groupNameMacro}</b> : name of the contest or category name"


    // Project Settings CMake
    const val addToCMakeMsg = "Add generated solution files to CMakeLists.txt"

    // Settings panels strings
    const val judgeComment = "" +
            "The judge program can be used to evaluate the participant's program performance instead of comparing its output to some correct output.<br>" +
            "You can access the following files:<br>" +
            "<b>input.txt</b> which contains the test input<br>" +
            "<b>correct.txt</b> which contains the solution's program output (if solution's program provided)<br>" +
            "<b>answer.txt</b> which contains the participant's program output (if problem is not interactive)<br>" +
            "If the problem is interactive, use stdin and stdout with flushing o communicate with the participant's program.<br>" +
            "You can provide a comment by writing output to <b>comment.txt</b><br>" +
            "The judge program should terminate with one of the following exit codes:<br>" +
            "<b>0</b> - CORRECT ANSWER<br>" +
            "<b>1</b> - WRONG ANSWER"
    const val generatorComment = "" +
            "The generator program is used to generate the input for a testcase.<br>" +
            "The stdin will contain exactly one number: the index of the stress testcase.<br>" +
            "This could be used for a Random's seed to make the generated testcases deterministic.<br>" +
            "The testcase input is taken by the stdout of the generator program."
    const val correctComment = "" +
            "The solution program should be a program that always answers correctly but might need to much time for bigger inputs. " +
            "(Hence, your generator's testcase inputs should not be to big for the solutions program not to need forever.)<br>" +
            "It reads the testcase input from and answers by printing to the files specified in the Generator tab.</b>"


    // Build Configuration Dialog strings
    val commandTemplateDesc = "" +
            "The executable in these commands are run in an isolated temporary directory. But not the command itself.<br>" +
            "So, relative path to executable won't work and should use <b>\$dir</b> to build absolute path.<br>" +
            "Make sure you wrap this absolute path with double quotes.<br><br>" +
            "Macros:<br>" +
            "<b>${R.keys.inputPathMacro}</b> : absolute path to a solution file with quotes.<br>" +
            "<b>${R.keys.dirUnquotedPathMacro}</b> : absolute path to the isolated temp directory without quotes.<br>" +
            "<b>${R.keys.dirPathMacro}</b> : \"<b>\$dir</b>\""

    const val buildCommandComment = "Run once before testing begins, usually should compile or generate an executable"
    const val executeCommandComment = "" +
            "Run for each testcase. usually should run the executable generated by Build Command. execution time of this command is considered for TLE verdict (Time Limit Exceeded)"

    // Common Err strings
    const val noReachErrMsg = "Execution should not have reached this line, $fileIssue"

    // Testing Process Strings

    fun solutionFilePathErrMsg(e: SolutionFilePathErr) = "" +
            "solutionFilePath in the run configuration \"${e.configName}\" has issues\n" +
            when (e) {
                is SolutionFilePathErr.BlankPathErr -> "It must not be empty"
                is SolutionFilePathErr.FileDoesNotExist -> "This file does not exists, ${e.pathString}"
                is SolutionFilePathErr.FileNotRegistered -> "AutoCp is not enabled for this file, ${e.pathString}"
                is SolutionFilePathErr.FormatErr -> "It is in invalid format."
            }

    fun langNotConfiguredErrMsg(e: LangNotConfiguredErr) = "" +
            "File Extension \".${e.extension}\" is not configured\n" +
            "Fix this issue at Settings/Preferences > Tools > AutoCp > Languages > +"

    // Testing Compile Strings

    fun commandReadyMsg(configName: String) = "" +
            "Ready to execute \"$configName\""

    fun startCompilingMsg(configName: String) = "" +
            "Building \"$configName\" ..."

    fun compileSuccessMsg(log: String, executionMills: Long) = "" +
            "Build completed in ${executionMills}ms\n" + log

    fun buildErrMsg(e: BuildErr) = "" +
            "Error while running the below command\n${e.command}\n\n" +
            when (e.err) {
                is ProcessRunnerErr.RuntimeErr -> e.err.localizedMessage
                is ProcessRunnerErr.TimeoutErr -> "Took longer than ${e.err.timeLimit}ms to execute"
                is ProcessRunnerErr.DeadProcessErr -> "Trying to run a process which is already dead, $fileIssue"
                else -> throw e.err
            }

    // Testing Verdict Strings
    fun verdictOneLine(verdict: Verdict) = when (verdict) {
        is Verdict.CorrectAnswer -> "[+] SUCCESS: CORRECT ANSWER"
        is Verdict.WrongAnswer -> "[-] FAILURE: WRONG ANSWER"
        is Verdict.TimeLimitErr -> "[-] FAILURE: TIME LIMIT EXCEEDED"
        is Verdict.RuntimeErr -> "[-] FAILURE: RUNTIME ERROR"
        is Verdict.InternalErr -> "[+/-] UNKNOWN: COULD NOT JUDGE"
    }
}

fun String.success(): String = "$this Successful"