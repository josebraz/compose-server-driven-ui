package com.josebraz.serverdrivenui.core.model

import com.josebraz.serverdrivenui.core.action.Operand
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AnyValue {
    override fun toString(): String
    fun toInt(): Int
    fun toFloat(): Float
    fun toDouble(): Double
    fun toOperand(): Operand

    companion object {
        fun fromAny(value: Any): AnyValue {
            return when (value) {
                is String -> StringValue(value)
                is Int -> NumberValue.IntValue(value)
                is Long -> NumberValue.IntValue(value.toInt())
                is Float -> NumberValue.FloatValue(value)
                is Double -> NumberValue.FloatValue(value.toFloat())
                is Operand -> {
                    when (value) {
                        is Operand.NumberOperand -> value.numberValue
                        is Operand.String -> StringValue(value.string)
                        is Operand.Var -> StringValue(value.identifier)
                    }
                }
                else -> UnknownValue
            }
        }
    }
}

fun <K> Map<K, Any>.toAnyValues(): Map<K, AnyValue> = this.mapValues {
    AnyValue.fromAny(it.value)
}

@Serializable
@SerialName("UnknownValue")
data object UnknownValue: AnyValue {
    override fun toInt(): Int = 0
    override fun toFloat(): Float = 0F
    override fun toDouble(): Double = 0.0
    override fun toOperand(): Operand = throw Exception()
}

@Serializable
@SerialName("String")
data class StringValue(
    val string: String
): AnyValue {
    override fun toString(): String = string
    override fun toInt(): Int = string.toInt()
    override fun toFloat(): Float = string.toFloat()
    override fun toDouble(): Double = string.toDouble()
    override fun toOperand(): Operand = Operand.String(string)
}

fun String.toAnyValue(): AnyValue = StringValue(this)
fun Int.toNumberValue(): NumberValue = NumberValue.IntValue(this)
fun Long.toNumberValue(): NumberValue = NumberValue.IntValue(this.toInt())
fun Float.toNumberValue(): NumberValue = NumberValue.FloatValue(this)
fun Double.toNumberValue(): NumberValue = NumberValue.FloatValue(this.toFloat())

fun String.toOperand(): Operand = this.toAnyValue().toOperand()
fun Int.toOperand(): Operand = this.toNumberValue().toOperand()
fun Long.toOperand(): Operand = this.toNumberValue().toOperand()
fun Float.toOperand(): Operand = this.toNumberValue().toOperand()
fun Double.toOperand(): Operand = this.toNumberValue().toOperand()

@Serializable
@SerialName("Number")
sealed interface NumberValue: AnyValue {
    operator fun plus(other: NumberValue): NumberValue
    operator fun minus(other: NumberValue): NumberValue
    operator fun times(other: NumberValue): NumberValue
    operator fun div(other: NumberValue): NumberValue
    operator fun rem(other: NumberValue): NumberValue

    override fun toOperand(): Operand = Operand.NumberOperand(this)

    @SerialName("Number.Int")
    data class IntValue(val int: Int): NumberValue {
        override fun plus(other: NumberValue): NumberValue {
            return when (other) {
                is FloatValue -> FloatValue(this.int + other.float)
                is IntValue -> IntValue(this.int + other.int)
            }
        }

        override fun minus(other: NumberValue): NumberValue {
            return when (other) {
                is FloatValue -> FloatValue(this.int - other.float)
                is IntValue -> IntValue(this.int - other.int)
            }
        }

        override fun times(other: NumberValue): NumberValue {
            return when (other) {
                is FloatValue -> FloatValue(this.int * other.float)
                is IntValue -> IntValue(this.int * other.int)
            }
        }

        override fun div(other: NumberValue): NumberValue {
            return when (other) {
                is FloatValue -> FloatValue(this.int / other.float)
                is IntValue -> IntValue(this.int / other.int)
            }
        }

        override fun rem(other: NumberValue): NumberValue {
            return when (other) {
                is FloatValue -> FloatValue(this.int % other.float)
                is IntValue -> IntValue(this.int % other.int)
            }
        }

        override fun toString(): String = int.toString()
        override fun toInt(): Int = int
        override fun toFloat(): Float = int.toFloat()
        override fun toDouble(): Double = int.toDouble()
    }

    @SerialName("Number.Float")
    data class FloatValue(val float: Float): NumberValue {
        override fun plus(other: NumberValue): NumberValue {
            return when (other) {
                is FloatValue -> FloatValue(this.float + other.float)
                is IntValue -> FloatValue(this.float + other.int)
            }
        }

        override fun minus(other: NumberValue): NumberValue {
            return when (other) {
                is FloatValue -> FloatValue(this.float - other.float)
                is IntValue -> FloatValue(this.float - other.int)
            }
        }

        override fun times(other: NumberValue): NumberValue {
            return when (other) {
                is FloatValue -> FloatValue(this.float * other.float)
                is IntValue -> FloatValue(this.float * other.int)
            }
        }

        override fun div(other: NumberValue): NumberValue {
            return when (other) {
                is FloatValue -> FloatValue(this.float / other.float)
                is IntValue -> FloatValue(this.float / other.int)
            }
        }

        override fun rem(other: NumberValue): NumberValue {
            return when (other) {
                is FloatValue -> FloatValue(this.float % other.float)
                is IntValue -> FloatValue(this.float % other.int)
            }
        }

        override fun toString(): String = float.toString()
        override fun toInt(): Int = float.toInt()
        override fun toFloat(): Float = float
        override fun toDouble(): Double = float.toDouble()
    }
}
