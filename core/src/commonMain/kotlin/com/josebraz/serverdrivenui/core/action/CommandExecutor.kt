package com.josebraz.serverdrivenui.core.action

import com.josebraz.serverdrivenui.core.model.AnyValue

interface CalcContext: Iterable<Map.Entry<String, Operand>> {
    operator fun get(identifier: String): Operand
    operator fun set(identifier: String, operand: Operand)
    operator fun plus(context: CalcContext): CalcContext
    override operator fun iterator(): Iterator<Map.Entry<String, Operand>>
}

fun MutableMap<String, AnyValue>.toOperand(): MutableMap<String, Operand> {
    return this.mapValuesTo(mutableMapOf()) { (_, value) -> value.toOperand() }
}

class CalcContextMap(
    private val map: MutableMap<String, Operand> = mutableMapOf()
): CalcContext {

    companion object {
        fun wrap(map: MutableMap<String, AnyValue>): CalcContext {
            return object : CalcContext {
                override fun get(identifier: String): Operand {
                    return map[identifier]!!.toOperand()
                }
                override fun set(identifier: String, operand: Operand) {
                    map[identifier] = operand.toAnyValue()
                }
                override fun plus(context: CalcContext): CalcContext {
                    return CalcContextMap(map.toOperand()) + context
                }
                override fun iterator(): Iterator<Map.Entry<String, Operand>> {
                    return map.toOperand().iterator()
                }
            }
        }
        fun fromArgs(vararg args: Operand): CalcContextMap {
            return CalcContextMap(args.withIndex().associateTo(mutableMapOf()) {
                "arg${it.index}" to it.value
            })
        }
    }
    override operator fun get(identifier: String): Operand = map[identifier]
        ?: throw RuntimeException("Variable '$identifier' not defined")
    override operator fun set(identifier: String, operand: Operand) {
        map[identifier] = operand
    }
    override operator fun plus(context: CalcContext): CalcContext {
        if (context !is CalcContextMap) return this
        return CalcContextMap((map + context.map).toMutableMap())
    }

    override fun iterator(): Iterator<Map.Entry<String, Operand>> = map.iterator()

    override fun toString(): String = map.toString()
}

fun calcContextOf(vararg pairs: Pair<String, Operand>): CalcContext {
    return CalcContextMap(mutableMapOf(*pairs))
}

class CommandExecutor {
    fun execute(ast: Ast, context: CalcContext, vararg args: Operand): Operand {
        val needSaveContext = args.isNotEmpty()
        val realContext = if (needSaveContext) {
            context + CalcContextMap.fromArgs(*args)
        } else {
            context
        }
        return when (ast) {
            is Operand -> ast.process(realContext)
            is Tree -> {
                val fromAssign = ast.operate is Operate.Assign
                ast.operate.calc(
                    op1 = if (fromAssign && ast.node1 is Operand.Var) {
                        ast.node1
                    } else {
                        execute(ast.node1, realContext)
                    },
                    op2 = execute(ast.node2, realContext),
                    context = realContext
                )
            }
            is Token -> throw IllegalStateException("Operand as node")
        }.also {
            if (needSaveContext) {
                context.forEach { (key, value) ->
                    context[key] = realContext[key]
                }
            }
        }
    }
}
