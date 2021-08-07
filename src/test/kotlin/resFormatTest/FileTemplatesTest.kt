package resFormatTest

import common.res.R
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.nio.file.Path
import kotlin.io.path.extension

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileTemplatesTest {
    lateinit var fileTemplatePaths: List<Path>

    @BeforeAll
    fun setup() {
        fileTemplatePaths = R.files.fileTemplates
    }

    @Test
    fun `only ft files must be present in fileTemplates j2ee directory`() {
        fileTemplatePaths.forEach {
            Assertions.assertEquals(it.extension, "ft", "${it.fileName} is not a ft file")
        }
    }

    @Test
    fun `file name must be in proper format`() {
        fileTemplatePaths.forEach {
            Assertions.assertTrue(
                Regex("").matchEntire(it.fileName.toString()) != null,
                "\"${it.fileName}\" is not in the format {PascalCasedLanguageName}.{languageFileExtension}.ft"
            )
        }
    }
}