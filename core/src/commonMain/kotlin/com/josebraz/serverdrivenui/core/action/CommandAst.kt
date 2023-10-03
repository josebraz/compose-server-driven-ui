package com.josebraz.serverdrivenui.core.action

import com.josebraz.serverdrivenui.core.model.AnyValue
import com.josebraz.serverdrivenui.core.model.NumberValue
import com.josebraz.serverdrivenui.core.model.toAnyValue


sealed interface Ast

data class Tree(
    val node1: Ast,
    val operate: Operate,
    val node2: Ast
): Ast {
    override fun toString(): String {
        return "($node1 $operate $node2)"
    }
}

val SimpleConcatRegex = """(?<!\\)\$(?!\{)([^\s]+)""".toRegex()
val ComplexConcatRegex = """(?<!\\)\$\{(.*?)\}""".toRegex()

sealed interface Operand: Ast {
    fun process(context: CalcContext): Operand = this
    fun toAnyValue(): AnyValue
    data class Var(val identifier: kotlin.String): Operand {
        override fun toString(): kotlin.String = identifier
        override fun process(context: CalcContext): Operand = kotlin.runCatching {
            context[identifier]
        }.getOrElse { this }
        override fun toAnyValue(): AnyValue = identifier.toAnyValue()
    }
    data class NumberOperand(
        val numberValue: NumberValue
    ): Operand {
        override fun toString(): kotlin.String = numberValue.toString()
        override fun toAnyValue(): AnyValue = numberValue
    }
    data class String(val string: kotlin.String): Operand {
        override fun toString(): kotlin.String = "\"$string\""
        override fun toAnyValue(): AnyValue = string.toAnyValue()
        override fun process(context: CalcContext): Operand {
            val resultSimple = SimpleConcatRegex.replace(string) { result ->
                val command = result.groupValues[1]
                val ast = CommandParser().parse(command)
                val operand = CommandExecutor().execute(ast, context)
                if (operand is String) operand.string else operand.toString()
            }
            val resultComplex = ComplexConcatRegex.replace(resultSimple) { result ->
                val command = result.groupValues[1]
                val ast = CommandParser().parse(command)
                val operand = CommandExecutor().execute(ast, context)
                if (operand is String) operand.string else operand.toString()
            }
            return String(resultComplex)
        }
    }
}

sealed interface Token: Ast

data object BracketStart: Token
data object BracketEnd: Token

sealed interface Operate: Token {
    fun calc(op1: Operand, op2: Operand, context: CalcContext): Operand

    data object Plus: Operate {
        override fun calc(op1: Operand, op2: Operand, context: CalcContext): Operand {
            val realOp1 = if (op1 is Operand.Var) context[op1.identifier] else op1
            val realOp2 = if (op2 is Operand.Var) context[op2.identifier] else op2
            return when {
                realOp1 is Operand.NumberOperand && realOp2 is Operand.NumberOperand -> {
                    Operand.NumberOperand(realOp1.numberValue + realOp2.numberValue)
                }
                realOp1 is Operand.String && realOp2 is Operand.String -> {
                    Operand.String(realOp1.string + realOp2.string)
                }
                else -> throw IllegalStateException("cal exception SUM $op1 $op2")
            }
        }
        override fun toString(): String = "+"
    }

    data object Minus: Operate {
        override fun calc(op1: Operand, op2: Operand, context: CalcContext): Operand {
            val realOp1 = if (op1 is Operand.Var) context[op1.identifier] else op1
            val realOp2 = if (op2 is Operand.Var) context[op2.identifier] else op2
            return when {
                realOp1 is Operand.NumberOperand && realOp2 is Operand.NumberOperand -> {
                    Operand.NumberOperand(realOp1.numberValue - realOp2.numberValue)
                }
                else -> throw IllegalStateException("cal exception MINUS $op1 $op2")
            }
        }
        override fun toString(): String = "-"
    }

    data object Times: Operate {
        override fun calc(op1: Operand, op2: Operand, context: CalcContext): Operand {
            val realOp1 = if (op1 is Operand.Var) context[op1.identifier] else op1
            val realOp2 = if (op2 is Operand.Var) context[op2.identifier] else op2
            return when {
                realOp1 is Operand.NumberOperand && realOp2 is Operand.NumberOperand -> {
                    Operand.NumberOperand(realOp1.numberValue * realOp2.numberValue)
                }
                else -> throw IllegalStateException("cal exception TIMES $op1 $op2")
            }
        }
        override fun toString(): String = "*"
    }

    data object Div: Operate {
        override fun calc(op1: Operand, op2: Operand, context: CalcContext): Operand {
            val realOp1 = if (op1 is Operand.Var) context[op1.identifier] else op1
            val realOp2 = if (op2 is Operand.Var) context[op2.identifier] else op2
            return when {
                realOp1 is Operand.NumberOperand && realOp2 is Operand.NumberOperand -> {
                    Operand.NumberOperand(realOp1.numberValue / realOp2.numberValue)
                }
                else -> throw IllegalStateException("cal exception DIV $op1 $op2")
            }
        }
        override fun toString(): String = "/"
    }

    data object Rem: Operate {
        override fun calc(op1: Operand, op2: Operand, context: CalcContext): Operand {
            val realOp1 = if (op1 is Operand.Var) context[op1.identifier] else op1
            val realOp2 = if (op2 is Operand.Var) context[op2.identifier] else op2
            return when {
                realOp1 is Operand.NumberOperand && realOp2 is Operand.NumberOperand -> {
                    Operand.NumberOperand(realOp1.numberValue % realOp2.numberValue)
                }
                else -> throw IllegalStateException("cal exception DIV $op1 $op2")
            }
        }
        override fun toString(): String = "%"
    }

    data object Assign: Operate {
        override fun calc(op1: Operand, op2: Operand, context: CalcContext): Operand {
            if (op1 !is Operand.Var) throw IllegalStateException("cal exception ASSIGN $op1 $op2")
            context[op1.identifier] = op2
            return op2
        }
        override fun toString(): String = "="
    }
}
