package com.josebraz.serverdrivenui.core.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Color")
@Immutable
data class Color internal constructor(
    val hex: String
){
    companion object {
        val Undefined = Color("")
    }
}

fun String.toColor(): Color = Color(this)