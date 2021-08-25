package gather

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.lang.Language
import com.intellij.lang.LanguageUtil
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.fileTypes.LanguageFileType
import common.res.R
import settings.langSettings.model.Lang
import kotlin.io.path.nameWithoutExtension

class FileTemplates : FileTemplateGroupDescriptorFactory {
    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val group = FileTemplateGroupDescriptor(GROUP_NAME, R.icons.logo16)

        // adding file templates in resources/fileTemplates/j2ee supported by the current IDE
        R.files.fileTemplates.filter {
            val fileType = FileTypeManager.getInstance().getFileTypeByFileName(it.nameWithoutExtension)

            if (fileType !is LanguageFileType)
                false
            else
                fileType.language != Language.ANY && LanguageUtil.isFileLanguage(fileType.language)

        }.forEach { group.addTemplate(it.nameWithoutExtension) }

        return group
    }


    companion object {
        const val GROUP_NAME = "AutoCp Templates"
    }
}

fun Lang.supportedFileTemplates(): List<FileTemplate> {
    val fileType = Language.findLanguageByID(langId)?.associatedFileType!!
    val manager = FileTemplateManager.getDefaultInstance()

    return listOf(
        *manager.allJ2eeTemplates,
        *manager.allTemplates,
        *manager.internalTemplates
    ).filter { template -> template.isTemplateOfType(fileType) }
}