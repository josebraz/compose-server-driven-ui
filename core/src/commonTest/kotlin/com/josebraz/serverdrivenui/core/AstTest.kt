package com.josebraz.serverdrivenui.core

import com.josebraz.serverdrivenui.core.action.CalcContext
import com.josebraz.serverdrivenui.core.action.CalcContextMap
import com.josebraz.serverdrivenui.core.action.CommandExecutor
import com.josebraz.serverdrivenui.core.action.CommandParser
import com.josebraz.serverdrivenui.core.action.Operand
import com.josebraz.serverdrivenui.core.action.calcContextOf
import com.josebraz.serverdrivenui.core.model.toNumberValue
import com.josebraz.serverdrivenui.core.model.toOperand
import kotlin.test.Test
import kotlin.test.assertEquals

class AstTest {

    private fun testEvaluate(
        command: String,
        expected: Operand,
        context: CalcContext = CalcContextMap()
    ): CalcContext {
        val ast = CommandParser().parse(command)
        val result = CommandExecutor().execute(ast, context)
        assertEquals(expected, result)
        return context
    }

    @Test
    fun assignTest() {
        val command = "test = 1 + 2 * 40.0 / 30.0 - 4"
        val expected = (-0.33333325).toOperand()
        val context = testEvaluate(command, expected)
        assertEquals(expected, context["test"])
    }

    @Test
    fun assignVarWithNumberTest() {
        val command = "test0 = 1 + 2 * 40.0 / 30.0 - 4"
        val expected = (-0.33333325).toOperand()
        val context = testEvaluate(command, expected)
        assertEquals(expected, context["test0"])
    }

    @Test
    fun bracketTest() {
        testEvaluate("(1 + 2 + 3 + 4) / 2", 5.toOperand())
        testEvaluate("(1 + 2 + 3 + 4) + (4 + 6)", 20.toOperand())
        testEvaluate("((1 + 2) * (3 + 4)) / (3)", 7.toOperand())
    }

    @Test
    fun stringProcessTest() {
        val context = calcContextOf("test" to "VALUE".toOperand())
        testEvaluate("\"\${1 + 2} \${(4 / 2 + 50)}\"", "3 52".toOperand())
        testEvaluate("\"\${test}\"", "VALUE".toOperand(), context)
        testEvaluate("\"\$test aaa\"", "VALUE aaa".toOperand(), context)
    }
}
