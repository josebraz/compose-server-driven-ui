package com.josebraz.serverdrivenui.core.modifier

import com.josebraz.serverdrivenui.core.model.Size
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Modifier.Padding")
data class Padding(
    val left: Size = Size.Undefined,
    val top: Size = Size.Undefined,
    val right: Size = Size.Undefined,
    val bottom: Size = Size.Undefined
): Element

fun Modifier.padding(all: Size): Modifier = Padding(all, all, all, all).then(this)

fun Modifier.padding(
    vertical: Size = Size.Undefined,
    horizontal: Size = Size.Undefined
): Modifier = Padding(horizontal, vertical, horizontal, vertical).then(this)

fun Modifier.padding(
    left: Size = Size.Undefined,
    top: Size = Size.Undefined,
    right: Size = Size.Undefined,
    bottom: Size = Size.Undefined
): Modifier = Padding(left, top, right, bottom).then(this)