package com.josebraz.serverdrivenui.core.modifier

import com.josebraz.serverdrivenui.core.model.Size
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Modifier.Size")
data class SizeModifier(
    val height: Size = Size.Undefined,
    val width: Size = Size.Undefined
): Element

fun Modifier.size(
    height: Size = Size.Undefined,
    width: Size = Size.Undefined
): Modifier = SizeModifier(height, width).then(this)