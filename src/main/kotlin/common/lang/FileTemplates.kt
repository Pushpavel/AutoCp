package common.lang

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.lang.Language
import common.res.R
import settings.langSettings.model.Lang
import kotlin.io.path.nameWithoutExtension

class FileTemplates : FileTemplateGroupDescriptorFactory {
    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val group = FileTemplateGroupDescriptor(GROUP_NAME, R.icons.logo16)
        
        // adding file templates in resources/fileTemplates/j2ee
        R.files.fileTemplates.forEach {
            group.addTemplate(it.nameWithoutExtension)
        }

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