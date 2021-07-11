package lang

import com.intellij.ide.fileTemplates.FileTemplateDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory
import icons.Icons

class FileTemplates : FileTemplateGroupDescriptorFactory {
    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val group = FileTemplateGroupDescriptor(GROUP_NAME, Icons.LogoIcon)

        // adding c++ template
        group.addTemplate(CPP)

        return group
    }


    companion object {
        const val GROUP_NAME = "AutoCp Templates"

        const val CPP = "C++.cpp"
    }
}