package tester.format

import tester.models.ResultCode

fun ResultCode.presentableString(): String {
    return when (this) {
        ResultCode.WRONG_ANSWER -> "[-] FAILURE: WRONG ANSWER"
        ResultCode.PROGRAM_CRASH -> "[-] FAILURE: PROGRAM CRASHED (RUNTIME ERROR)"
        ResultCode.CORRECT_ANSWER -> "[+] SUCCESS: CORRECT ANSWER"
    }
}