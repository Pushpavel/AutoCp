package tester.judge

enum class Verdict {
    CORRECT_ANSWER,
    WRONG_ANSWER,
    RUNTIME_ERROR;

    companion object {
        fun Verdict.presentableString(): String {
            return when (this) {
                CORRECT_ANSWER -> "[+] SUCCESS: CORRECT ANSWER"
                WRONG_ANSWER -> "[-] FAILURE: WRONG ANSWER"
                RUNTIME_ERROR -> "[-] FAILURE: RUNTIME ERROR"
            }
        }
    }
}