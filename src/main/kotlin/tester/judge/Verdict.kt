package tester.judge

/**
 * Enum for variety of Verdicts that can be presented to the user
 */
enum class Verdict {
    CORRECT_ANSWER,
    WRONG_ANSWER,
    RUNTIME_ERROR,
    NO_VERDICT,
    TIME_LIMIT_EXCEEDED;

    companion object {
        fun Verdict.presentableString(): String {
            return when (this) {
                CORRECT_ANSWER -> "[+] SUCCESS: CORRECT ANSWER"
                WRONG_ANSWER -> "[-] FAILURE: WRONG ANSWER"
                RUNTIME_ERROR -> "[-] FAILURE: RUNTIME ERROR"
                NO_VERDICT -> "[+/-] UNKNOWN: COULD NOT JUDGE"
                TIME_LIMIT_EXCEEDED -> "[-] FAILURE: TIME LIMIT EXCEEDED"
            }
        }
    }
}