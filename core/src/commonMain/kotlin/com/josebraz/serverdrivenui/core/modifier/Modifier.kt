package com.josebraz.serverdrivenui.core.modifier

import androidx.compose.runtime.Immutable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

@Serializable(with = ModifierSerializer::class)
@Immutable
interface Modifier {

    fun <R> fold(initial: R, operation: (R, Element) -> R): R
    infix fun then(other: Modifier): Modifier =
        if (other === Modifier) this else CombinedModifier(this, other)

    @Serializable
    companion object: Modifier {
        override fun <R> fold(initial: R, operation: (R, Element) -> R): R = initial
        override infix fun then(other: Modifier): Modifier = other
        override fun toString(): String = "Modifier"
    }
}

@Serializable
sealed interface Element : Modifier {
    override fun <R> fold(initial: R, operation: (R, Element) -> R): R = operation(initial, this)
}

private class CombinedModifier(
    private val outer: Modifier,
    private val inner: Modifier
) : Modifier {
    override fun <R> fold(initial: R, operation: (R, Element) -> R): R =
        inner.fold(outer.fold(initial, operation), operation)

    override fun equals(other: Any?): Boolean =
        other is CombinedModifier && outer == other.outer && inner == other.inner

    override fun hashCode(): Int = outer.hashCode() + 31 * inner.hashCode()

    override fun toString() = "[" + fold("") { acc, element ->
        if (acc.isEmpty()) element.toString() else "$acc, $element"
    } + "]"
}

object ModifierSerializer : KSerializer<Modifier> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Modifier", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Modifier) {
        val list = "[" + value.fold("") { acc, element ->
            val stringElement = Json.encodeToString(element)
            if (acc.isEmpty()) stringElement else "$acc, $stringElement"
        } + "]"
        encoder.encodeString(list)
    }

    override fun deserialize(decoder: Decoder): Modifier {
        val string = decoder.decodeString().replace("\\", "")
        val modifierList = Json.decodeFromString(ListSerializer(Element.serializer()), string)
        return modifierList.reduce<Modifier, Modifier> { acc, element ->
            acc.then(element)
        }
    }
}
