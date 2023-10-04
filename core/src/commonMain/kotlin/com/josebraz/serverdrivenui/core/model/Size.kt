package com.josebraz.serverdrivenui.core.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = SizeAsStringSerializer::class)
@SerialName("Size")
@Immutable
data class Size internal constructor(
    val value: Int,
    val type: String
) {
    companion object {
        val Undefined = Size(0, "")
    }

    override fun toString(): String = "${value}.${type}"
}

object SizeAsStringSerializer : KSerializer<Size> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Size", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Size) {
        encoder.encodeString("${value.value}.${value.type}")
    }

    override fun deserialize(decoder: Decoder): Size {
        val (value, type) = decoder.decodeString().split(".")
        return Size(value.toInt(), type)
    }
}

val Int.dp: Size get() = Size(this, "dp")
val Int.px: Size get() = Size(this, "px")
val Int.sp: Size get() = Size(this, "sp")