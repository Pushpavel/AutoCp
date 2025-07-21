package com.github.pushpavel.autocp.tool.ui

import com.github.pushpavel.autocp.build.settings.LangSettings
import com.github.pushpavel.autocp.common.helpers.mainScope
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.common.ui.helpers.allowOnlyPositiveIntegers
import com.github.pushpavel.autocp.common.ui.helpers.onChange
import com.github.pushpavel.autocp.database.SolutionFiles
import com.github.pushpavel.autocp.database.models.JudgeSettings
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
import javax.swing.JComboBox

class JudgeSettingsPanel(project: Project, pathString: String, refreshCallback: () -> Unit) :
    Disposable {

    private val solutionFiles = SolutionFiles.getInstance(project)
    private val flow = solutionFiles.listenFlow(pathString)

    private var solutionFile: SolutionFile? = null
    private var settings: JudgeSettings? = null

    private val editorFactory = EditorFactory.getInstance()
    private val judgeDoc = editorFactory.createDocument("")
    private val judgeEditor = editorFactory.createEditor(judgeDoc).apply { customizeEditor() }

    private val judgeExtension: JComboBox<String> = ComboBox(
        LangSettings.instance.langs.values.map { it.extension }.toTypedArray()
    )

    private var resetting = false

    private val scope = mainScope()

    private val header: DialogPanel = panelHeader(flow, scope)

    private val body: DialogPanel = panel {
        indent {

            row { label("Constraints:") }
            row {
                panel {
                    row {
                        label("").applyToComponent { icon = R.icons.clock }
                        intTextField().bindIntText(
                            MutableProperty(
                                getter = { settings?.timeLimit?.toInt() ?: 0 },
                                setter = { settings = settings?.copy(timeLimit = it.toLong()) }
                            )
                        ).applyToComponent {
                            allowOnlyPositiveIntegers()
                            document.onChange { apply() }
                        }
                        label("ms")
                    }
                }
            }.bottomGap(BottomGap.MEDIUM)
            row { panel {
                row("Input") {
                    textField().align(AlignX.FILL).bindText(
                        MutableProperty(
                            getter = { settings?.inputFile ?: "" },
                            setter = { settings = settings?.copy(inputFile = it.ifEmpty { null }) }
                        )
                    ).applyToComponent {
                        document.onChange { apply() }
                    }
                    contextHelp("Testcase input will be delivered by this file (leave empty for stdin)")
                }
                row("Output") {
                    textField().align(AlignX.FILL).bindText(
                        MutableProperty(
                            getter = { settings?.outputFile ?: "" },
                            setter = { settings = settings?.copy(outputFile = it.ifEmpty { null }) }
                        )
                    ).applyToComponent {
                        document.onChange { apply() }
                    }
                    contextHelp("Program's output will be read from this file (leave empty for stdout)")
                }
            } }.bottomGap(BottomGap.MEDIUM)
            row { label("Judge program:") }.topGap(TopGap.MEDIUM)
            row { panel {
                row {
                    checkBox("Interactive problem").bindSelected(
                        MutableProperty(
                            getter = { settings?.isInteractive ?: false },
                            setter = { settings = settings?.copy(isInteractive = it) }
                        )
                    ).onChanged { apply() }
                }
                row {
                    checkBox("Prefer judge program over output").bindSelected(
                        MutableProperty(
                            getter = { settings?.preferJudgeOverOutput ?: false },
                            setter = { settings = settings?.copy(preferJudgeOverOutput = it) }
                        )
                    ).onChanged { apply() }
                }
                row {
                    judgeEditor.headerComponent = programEditorHeader("Judge program code", judgeExtension)
                    cell(judgeEditor.component)
                        .align(AlignX.FILL)
                        .comment(R.strings.judgeComment)
                }
            } }.bottomGap(BottomGap.MEDIUM)
            row {
                button("Reset All Settings") {
                    solutionFile?.pathString?.let { it1 -> solutionFiles.remove(it1) }
                    refreshCallback()
                }
            }
        }
    }

    val component = BorderLayoutPanel().apply {
        add(header, BorderLayout.PAGE_START)
        add(JBScrollPane().apply {
            setViewportView(body)
        }, BorderLayout.CENTER)
    }

    init {
        judgeExtension.addActionListener {
            settings = settings?.copy(judgeProgram = settings!!.judgeProgram.copy(languageExtension = judgeExtension.selectedItem as String))
            apply()
        }
        scope.launch { flow.collect { reset(it) } }
        judgeDoc.onChanged(this) { text ->
            if (settings == null) return@onChanged
            settings = settings!!.copy(judgeProgram = settings!!.judgeProgram.copy(code = text.ifEmpty { null }))
            apply()
        }
    }

    fun apply() {
        if (resetting || settings == null) return

        solutionFile?.let {
            header.apply()
            body.apply()

            solutionFile = it.copy(judgeSettings = settings!!)

            solutionFiles.update(solutionFile!!)
        }
    }

    private fun reset(solutionFile: SolutionFile?) {
        resetting = true
        this.solutionFile = solutionFile

        settings = solutionFile?.judgeSettings

        header.reset()
        body.reset()
        judgeExtension.selectedItem = settings?.judgeProgram?.languageExtension
        runUndoTransparentWriteAction {
            judgeDoc.setText(settings?.judgeProgram?.code ?: "")
        }
        resetting = false
    }

    override fun dispose() {
        editorFactory.releaseEditor(judgeEditor)
        scope.cancel()
    }
}
