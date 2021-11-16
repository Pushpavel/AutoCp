package resFormatTest

import com.github.pushpavel.autocp.common.res.R
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.nio.file.Path

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileTemplatesTest {
    private lateinit var fileTemplatePaths: List<Path>

    @BeforeAll
    fun setup() {
        fileTemplatePaths = R.files.fileTemplates
    }

    @Test
    fun `file name must be in proper format`() {
        fileTemplatePaths.forEach {
            Assertions.assertTrue(
                Regex("${R.keys.fileTemplateName}_[A-Z0-9]+\\.[a-z0-9]+\\.(ft|html)").matchEntire(it.fileName.toString()) != null,
                "\"${it.fileName}\" is not in the format ${R.keys.fileTemplateName}_{languageFileExtensionInCapitals}.{languageFileExtension}.(ft | html)"
            )
        }
    }
}