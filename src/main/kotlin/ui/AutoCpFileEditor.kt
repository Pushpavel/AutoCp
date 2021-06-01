package ui

import com.intellij.diff.util.FileEditorBase
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.layout.panel
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.JBFont
import javax.swing.JComponent
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel


class AutoCpFileEditor(private val project: Project, private val solutionFile: VirtualFile) : FileEditorBase() {
    companion object {
        private const val NAME = "Testcases"
    }

    private val autoCp by lazy {
//        project.service<AutoCpFilesService>().getAutoCp(solutionFile.nameWithoutExtension)
    }

    private val window: JComponent by lazy {
        val root = DefaultMutableTreeNode("Contacts") // root node


        val contact1 = DefaultMutableTreeNode("Contact # 1") // level 1 node

        val nickName1 = DefaultMutableTreeNode("drocktapiff") // level 2 (leaf) node

        contact1.add(nickName1)

        val contact2 = DefaultMutableTreeNode("Contact # 2")
        val nickName2 = DefaultMutableTreeNode("dic19")
        contact2.add(nickName2)

        root.add(contact1)
        root.add(contact2)

        val model = DefaultTreeModel(root)
        val tree = Tree(model)
        return@lazy panel {
            row {
                label("This is it", JBFont.h0().asBold()).withLeftGap()
            }
            row {
                tree()
            }
        }
    }


    override fun getName() = NAME

    override fun getComponent() = window

    override fun getPreferredFocusedComponent() = component

    override fun getFile() = solutionFile
}