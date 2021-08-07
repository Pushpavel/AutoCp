package common.lang

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.lang.Language
import common.res.R
import settings.langSettings.model.Lang

class FileTemplates : FileTemplateGroupDescriptorFactory {
    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val group = FileTemplateGroupDescriptor(GROUP_NAME, R.icons.logo16)
        // adding c++ template
        group.addTemplate(CPP)

        return group
    }


    companion object {
        const val GROUP_NAME = "AutoCp Templates"

        const val CPP = "C++.cpp"
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