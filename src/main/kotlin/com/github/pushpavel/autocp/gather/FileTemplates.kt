package com.github.pushpavel.autocp.gather

import com.github.pushpavel.autocp.build.settings.LangSettings
import com.github.pushpavel.autocp.common.res.R
import com.intellij.ide.actions.CreateFileAction.MkDirs
import com.intellij.ide.fileTemplates.*
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.fileTypes.UnknownFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.util.IncorrectOperationException
import org.apache.velocity.runtime.parser.ParseException

class FileTemplates : FileTemplateGroupDescriptorFactory {
    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val group = FileTemplateGroupDescriptor(GROUP_NAME, R.icons.logo16)

        // adding file templates in resources/fileTemplates/j2ee supported by the current IDE
        LangSettings.instance.defaultLangs.map { it.key }
            .filter { extension ->
                val fileType = FileTypeManager.getInstance().getFileTypeByExtension(extension)
                fileType !is UnknownFileType
            }.forEach { group.addTemplate("${R.keys.fileTemplateName}_${it.uppercase()}.$it") }

        return group
    }


    companion object {
        const val GROUP_NAME = "AutoCp Templates"

        private fun cpTemplateFromExtensionOrNull(extension: String, project: Project): FileTemplate? {
            val templateName = "${R.keys.fileTemplateName}_${extension.uppercase()}.$extension"
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

        fun cpTemplateForExtension(extension: String, project: Project): FileTemplate {
            return cpTemplateFromExtensionOrNull(extension, project)
                ?: FileTemplateManager.getInstance(project)
                    .addTemplate("${R.keys.fileTemplateName}_${extension.uppercase()}.$extension", extension)
        }

        fun createFileFromTemplate(
            _name: String?,
            template: FileTemplate,
            _dir: PsiDirectory,
            props: Map<String, Any>
        ): PsiFile? {
            var name = _name
            var dir = _dir
            if (name != null) {
                val mkdirs = MkDirs(name, dir)
                name = mkdirs.newName
                dir = mkdirs.directory
            }
            val project = dir.project
            try {
                return FileTemplateUtil.createFromTemplate(
                    template,
                    name,
                    FileTemplateManager.getInstance(project).defaultProperties.apply {
                        putAll(props)
                    },
                    dir
                ).containingFile
            } catch (e: ParseException) {
                throw IncorrectOperationException("Error parsing file template: " + e.message, e as Throwable)
            } catch (e: IncorrectOperationException) {
                throw e
            } catch (e: NullPointerException) {
                throw IncorrectOperationException(R.notify.velocityNullPointerMsg)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

    }
}