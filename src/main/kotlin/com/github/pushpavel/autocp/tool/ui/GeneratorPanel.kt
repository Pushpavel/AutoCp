package com.github.pushpavel.autocp.tool.ui

import com.github.pushpavel.autocp.build.settings.LangSettings
import com.github.pushpavel.autocp.common.helpers.mainScope
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.common.ui.helpers.allowOnlyPositiveIntegers
import com.github.pushpavel.autocp.common.ui.helpers.onChange
import com.github.pushpavel.autocp.database.SolutionFiles
import com.github.pushpavel.autocp.database.models.Generator
import com.github.pushpavel.autocp.database.models.SolutionFile
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.runUndoTransparentWriteAction
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.dsl.builder.*
import com.intellij.util.ui.components.BorderLayoutPanel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.awt.BorderLayout

class GeneratorPanel(project: Project, pathString: String): Disposable {

    private val solutionFiles = SolutionFiles.getInstance(project)
    private val flow = solutionFiles.listenFlow(pathString)

    private var solutionFile: SolutionFile? = null;
    private var generator: Generator? = null

    private val editorFactory = EditorFactory.getInstance()
    private val generatorDoc = editorFactory.createDocument("")
    private val generatorEditor = editorFactory.createEditor(generatorDoc).apply { customizeEditor() }
    private val correctDoc = editorFactory.createDocument("")
    private val correctEditor = editorFactory.createEditor(correctDoc).apply { customizeEditor() }

    private val generatorExtension: ComboBox<String> = ComboBox(
        LangSettings.instance.langs.values.map { it.extension }.toTypedArray()
    )
    private val correctExtension: ComboBox<String> = ComboBox(
        LangSettings.instance.langs.values.map { it.extension }.toTypedArray()
    )

    private var resetting = false;

    private val scope = mainScope()

    private val header: DialogPanel = panelHeader(flow, scope)

    private val body: DialogPanel = panel {
        indent {
            row {
                label("Stress testing testcase amount")
                intTextField().bindIntText(
                    MutableProperty(
                        getter = { generator?.stressTestcaseAmount ?: 0 },
                        setter = { generator = generator?.copy(stressTestcaseAmount = it) }
                    )
                ).applyToComponent {
                    allowOnlyPositiveIntegers()
                    document.onChange { apply() }
                }
            }
            row {
                checkBox("Stop on first failing testcase").bindSelected(
                    MutableProperty(
                        getter = { generator?.haltOnFailing ?: true },
                        setter = { generator = generator?.copy(haltOnFailing = it) }
                    )
                ).onChanged { apply() }
            }
            row {
                generatorEditor.headerComponent = programEditorHeader("Generator program code", generatorExtension)
                cell(generatorEditor.component)
                    .align(AlignX.FILL)
                    .comment(R.strings.generatorComment)
            }
            row {
                correctEditor.headerComponent = programEditorHeader("Solution program code", correctExtension)
                cell(correctEditor.component)
                    .align(AlignX.FILL)
                    .comment(R.strings.correctComment)
            }
            row {
                checkBox("Run static testcases as well").bindSelected(
                    MutableProperty(
                        getter = { generator?.useStaticTestcases ?: false },
                        setter = { generator = generator?.copy(useStaticTestcases = it) }
                    )
                ).onChanged { apply() }
            }.bottomGap(BottomGap.MEDIUM)
        }
    }

    val component = BorderLayoutPanel().apply {
        add(header, BorderLayout.PAGE_START)
        add(JBScrollPane().apply {
            setViewportView(body)
        }, BorderLayout.CENTER)
    }

    init {
        correctExtension.addActionListener {
            generator = generator?.copy(correctProgram = generator!!.correctProgram.copy(languageExtension = correctExtension.selectedItem as String))
            apply()
        }
        generatorExtension.addActionListener {
            generator = generator?.copy(generatorProgram = generator!!.generatorProgram.copy(languageExtension = generatorExtension.selectedItem as String))
            apply()
        }
        scope.launch { flow.collect { reset(it) } }
        correctDoc.onChanged(this) { text ->
            if (generator == null) return@onChanged
            generator = generator!!.copy(correctProgram = generator!!.correctProgram.copy(code = text.ifEmpty { null }))
            apply()
        }
        generatorDoc.onChanged(this) { text ->
            if (generator == null) return@onChanged
            generator = generator!!.copy(generatorProgram = generator!!.generatorProgram.copy(code = text.ifEmpty { null }))
            apply()
        }
    }

    fun apply() {
        if (resetting || generator == null) return
        solutionFile?.let {
            header.apply()
            body.apply()

            solutionFile = it.copy(generator = generator!!)

            solutionFiles.update(solutionFile!!)
        }
    }

    private fun reset(solutionFile: SolutionFile?) {
        resetting = true
        this.solutionFile = solutionFile

        generator = solutionFile?.generator

        header.reset()
        body.reset()
        generatorExtension.selectedItem = generator?.generatorProgram?.languageExtension
        correctExtension.selectedItem = generator?.correctProgram?.languageExtension
        runUndoTransparentWriteAction {
            generatorDoc.setText(generator?.generatorProgram?.code ?: "")
            correctDoc.setText(generator?.correctProgram?.code ?: "")
        }
        resetting = false
    }

    override fun dispose() {
        editorFactory.releaseEditor(generatorEditor)
        editorFactory.releaseEditor(correctEditor)
        scope.cancel()
    }

}
