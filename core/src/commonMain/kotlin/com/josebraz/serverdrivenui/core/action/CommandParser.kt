package com.josebraz.serverdrivenui.core.action

import com.josebraz.serverdrivenui.core.model.NumberValue

class CommandParser {

    companion object {
        val TwoArgsOperators by lazy {
            listOf(
                Operate.Times,
                Operate.Div,
                Operate.Rem,
                Operate.Plus,
                Operate.Minus,
                Operate.Assign
            )
        }

        val TokenMap by lazy {
            mapOf(
                "*" to Operate.Times,
                "+" to Operate.Plus,
                "-" to Operate.Minus,
                "/" to Operate.Div,
                "%" to Operate.Rem,
                "=" to Operate.Assign,
                "(" to BracketStart,
                ")" to BracketEnd,
            )
        }
    }

    fun parse(command: String): Ast {
        val tokensString = command.split("\"").flatMapIndexed { index: Int, s: String ->
            when {
                s.isEmpty() -> emptyList()
                index % 2 == 0 -> s.split("\\s+|(?<=[(])|(?=[)])".toRegex())
                else -> listOf("\"$s\"")
            }
        }.filterNot { it.isEmpty() }
        val nodes = tokensString.mapTo(mutableListOf()) { tokenString ->
            val token = tokenString.trim()
            TokenMap[token] ?: run {
                when {
                    token.startsWith('"') && token.endsWith('"') -> {
                        Operand.String(token.removePrefix("\"").removeSuffix("\""))
                    }
                    token.matches("[0-9]+".toRegex()) -> Operand.NumberOperand(NumberValue.IntValue(token.toInt()))
                    token.matches("[0-9]+[.][0-9]*".toRegex()) -> Operand.NumberOperand(NumberValue.FloatValue(token.toFloat()))
                    else -> Operand.Var(token)
                }
            }
        }

        return ast(nodes)
    }

    private fun ast(nodes: List<Ast>): Ast {
        if (nodes.isEmpty()) throw IllegalStateException("Empty AST")
        if (nodes.size == 1) return nodes.first()
        if (nodes.size == 3) return Tree(nodes[0], nodes[1] as Operate, nodes[2])

        var startIndex: Int = nodes.indexOfLast { it == BracketStart }
        var endIndex: Int = -1
        var rightStartIndex: Int = -1
        var leftEndIndex: Int = -1
        if (startIndex >= 0) {
            endIndex = nodes.subList(startIndex+1, nodes.size)
                .indexOfFirst { it == BracketEnd } + startIndex+1
            if (endIndex < 0) {
                throw IllegalStateException("Start bracket without end")
            }
            rightStartIndex = endIndex+1
            leftEndIndex = startIndex-1
            startIndex++
            endIndex--
        } else {
            for (operate in TwoArgsOperators) {
                val index = nodes.indexOfFirst { it == operate }
                if (index < 0) continue
                startIndex = index-1
                endIndex = index+1
                rightStartIndex = endIndex+1
                leftEndIndex = startIndex-1
                break
            }
        }

        val current: Ast = ast(nodes.subList(startIndex, endIndex+1))
        val left = if (leftEndIndex >= 0) {
            nodes.subList(0, leftEndIndex+1)
        } else {
            emptyList()
        }
        val right = if (rightStartIndex < nodes.size) {
            nodes.subList(rightStartIndex, nodes.size)
        } else {
            emptyList()
        }

        return ast(left + current + right)
    }
}
