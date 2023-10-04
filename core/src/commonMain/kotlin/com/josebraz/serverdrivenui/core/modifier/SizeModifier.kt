package com.josebraz.serverdrivenui.core.modifier

import androidx.compose.runtime.Immutable
import com.josebraz.serverdrivenui.core.model.Size
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Modifier.Size")
@Immutable
data class SizeModifier(
    val height: Size = Size.Undefined,
    val width: Size = Size.Undefined
): Element

fun Modifier.size(
    height: Size = Size.Undefined,
    width: Size = Size.Undefined
): Modifier = SizeModifier(height, width).then(this)

@Serializable
@SerialName("Modifier.Fill")
@Immutable
data class FillModifier(
    val height: Float = -1F,
    val width: Float = -1F
): Element

fun Modifier.fillMaxWidth(fraction: Float = 1F): Modifier {
    return FillModifier(width = fraction).then(this)
}

fun Modifier.fillMaxHeight(fraction: Float = 1F): Modifier {
    return FillModifier(height = fraction).then(this)
}

fun Modifier.fillMaxSize(fraction: Float = 1F): Modifier {
    return FillModifier(width = fraction, height = fraction).then(this)
}