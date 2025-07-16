package com.github.pushpavel.autocp.tester.errors

sealed class TestGenerationErr(message: String? = null) : Exception(message) {

    class GeneratorNotProvided(message: String? = null) : TestGenerationErr(message)
    class GeneratorFailed(message: String? = null) : TestGenerationErr(message)
    class CorrectProgramFailed(message: String? = null) : TestGenerationErr(message)

}
