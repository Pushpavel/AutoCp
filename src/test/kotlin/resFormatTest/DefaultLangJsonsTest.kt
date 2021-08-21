package resFormatTest

import common.res.R
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.*
import settings.generalSettings.AutoCpGeneralSettings
import settings.langSettings.model.Lang
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readText

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultLangJsonsTest {

    lateinit var jsonPaths: List<Path>

    @BeforeAll
    fun setup() {
        jsonPaths = R.files.langJsons
    }

    @Test
    fun `only json files should be present inside languages folder`() {
        jsonPaths.forEach {
            Assertions.assertEquals(it.extension, "json", "${it.fileName} is not a json file")
        }
    }

    @Test
    fun `json Files are parsed without error`() {
        jsonPaths.map { it.readText() }.forEach {
            Assertions.assertDoesNotThrow { Json.decodeFromString<Lang>(it) }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class LangValidationTest {
        lateinit var langs: List<Lang>

        @BeforeAll
        fun setup() {
            langs = jsonPaths.map { it.readText() }.map { Json.decodeFromString(it) }
        }

        @Test
        fun `specified fileTemplate exists`() {
            val fileTemplateNames = R.files.fileTemplates.map { it.nameWithoutExtension.substringBeforeLast(".") }
            langs.forEach {
                if (it.fileTemplateName != null)
                    Assertions.assertTrue(
                        fileTemplateNames.contains(it.fileTemplateName),
                        "File Template ${it.fileTemplateName} in ${it.langId} does not exists in resources/fileTemplates/j2ee folder, make sure to specify the fileTemplateName without extension, ex- \"C++.cpp.ft\" must be specified as \"C++\""
                    )
            }
        }

        @Test
        fun `at least one Build Configuration must be specified`() {
            langs.forEach {
                Assertions.assertTrue(it.buildConfigs.isNotEmpty(), "${it.langId} must contain atleast one buildConfig")
            }
        }

        @Test
        fun `defaultBuildConfigId must be valid`() {
            for (lang in langs)
                Assertions.assertNotNull(
                    lang.buildConfigs[lang.defaultBuildConfigId],
                    "defaultBuildConfigId ${lang.defaultBuildConfigId} in ${lang.langId} does not correspond to any of the buildConfigs specified"
                )
        }

        @Nested
        inner class BuildConfigValidationTest {

            @Test
            fun `BuildConfig id must match key of the buildConfigs MapEntry`() {
                for (lang in langs)
                    for (it in lang.buildConfigs)
                        Assertions.assertEquals(it.key, it.value.id)
            }

            @Test
            fun `BuildConfig id must be valid and unique across all Languages`() {
                val ids = langs.flatMap { it.buildConfigs.keys }
                Assertions.assertEquals(ids.size, ids.toSet().size, run {
                    val duplicateId = ids.firstOrNull {
                        val occurrences = ids.count { id -> id == it }
                        occurrences > 1
                    }

                    "Build Configuration with id \"$duplicateId\" exists for more than one Build Configuration across all languages"
                })
            }

            @Test
            fun `BuildConfig name must be valid and unique`() {
                for (lang in langs) {
                    val names = lang.buildConfigs.values.map {
                        Assertions.assertTrue(
                            it.name.isNotBlank(),
                            "Build Configuration must not have a blank name, langId = ${lang.langId}, build config id = ${it.id}"
                        )
                        it.name
                    }
                    Assertions.assertEquals(names.size, names.toSet().size, run {
                        val duplicateId = names.firstOrNull {
                            val occurrences = names.count { id -> id == it }
                            occurrences > 1
                        }

                        "Build Configuration with name \"$duplicateId\" exists for more than one Build Configuration in ${lang.langId}"
                    })
                }
            }

            @Test
            fun `BuildConfig commandTemplate must be valid`() {
                for (lang in langs) {
                    for (config in lang.buildConfigs.values) {
                        val input = config.commandTemplate.contains(AutoCpGeneralSettings.INPUT_PATH_KEY)
                        val output = config.commandTemplate.contains(AutoCpGeneralSettings.OUTPUT_PATH_KEY)

                        val errorMessage = when {
                            !input -> "${AutoCpGeneralSettings.INPUT_PATH_KEY} missing, This will be replaced with path to solution file ex- \"C:\\solution.cpp\""
                            !output -> "${AutoCpGeneralSettings.OUTPUT_PATH_KEY} missing, This will be replaced with path for the executable ex- \"C:\\temp\\output.exe\""
                            else -> null
                        }

                        Assertions.assertNull(
                            errorMessage,
                            "langId = ${lang.langId}, buildConfig id = ${config.id}, $errorMessage"
                        )
                    }
                }
            }
        }
    }


}