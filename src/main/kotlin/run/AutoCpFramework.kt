package run

import com.intellij.icons.AllIcons
import com.intellij.ide.fileTemplates.FileTemplateDescriptor
import com.intellij.lang.Language
import com.intellij.openapi.module.Module
import com.intellij.psi.PsiElement
import com.intellij.testIntegration.TestFramework
import common.Constants

class AutoCpFramework : TestFramework {
    override fun getName() = Constants.FrameworkName

    override fun getIcon() = AllIcons.General.Modified

    override fun isLibraryAttached(module: Module) = false

    override fun getLibraryPath(): String? = null

    override fun getDefaultSuperClass(): String? = null

    override fun isTestClass(clazz: PsiElement) = false

    override fun isPotentialTestClass(clazz: PsiElement) = false

    override fun findSetUpMethod(clazz: PsiElement): PsiElement? = null

    override fun findTearDownMethod(clazz: PsiElement): PsiElement? = null
    override fun findOrCreateSetUpMethod(clazz: PsiElement): PsiElement? = null

    override fun getSetUpMethodFileTemplateDescriptor() = null

    override fun getTearDownMethodFileTemplateDescriptor() = null

    override fun getTestMethodFileTemplateDescriptor() = FileTemplateDescriptor("unused")

    override fun isIgnoredMethod(element: PsiElement?) = false

    override fun isTestMethod(element: PsiElement?) = false

    override fun getLanguage(): Language = Language.ANY
}