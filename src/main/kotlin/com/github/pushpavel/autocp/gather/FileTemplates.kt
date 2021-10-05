package com.github.pushpavel.autocp.gather

import com.github.pushpavel.autocp.common.res.R
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.lang.Language
import com.intellij.lang.LanguageUtil
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.project.Project
import kotlin.io.path.name

class FileTemplates : FileTemplateGroupDescriptorFactory {
    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val group = FileTemplateGroupDescriptor(GROUP_NAME, R.icons.logo16)

        // adding file templates in resources/fileTemplates/j2ee supported by the current IDE
        // TODO: get extensions from default lang Settings
        R.files.fileTemplates.map { it.fileName.name.run { split('.')[1] } }.filter { extension ->
            val fileType = FileTypeManager.getInstance().getFileTypeByExtension(extension)
            if (fileType !is LanguageFileType)
                false
            else
                fileType.language != Language.ANY && LanguageUtil.isFileLanguage(fileType.language)

        }.forEach { group.addTemplate("${R.keys.fileTemplateName}.${it}") }

        return group
    }


    companion object {
        const val GROUP_NAME = "AutoCp Templates"

        private fun cpTemplateFromExtensionOrNull(extension: String, project: Project): FileTemplate? {
            val templateName = "${R.keys.fileTemplateName}.${extension}"
            val m = FileTemplateManager.getInstance(project)
            return try {
                m.getInternalTemplate(templateName)
            } catch (e: IllegalStateException) {
                try {
                    m.getJ2eeTemplate(templateName)
                } catch (e: IllegalStateException) {
                    null
                }
            }
        }

        fun cpTemplateFromExtension(extension: String, project: Project): FileTemplate {
            return cpTemplateFromExtensionOrNull(extension, project)
                ?: FileTemplateManager.getInstance(project).addTemplate("abc.$extension", extension)
        }
    }
}