package com.josebraz.serverdrivenui.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Color")
data class Color internal constructor(
    val hex: String
){
    companion object {
        val Undefined = Color("")
    }
}

fun String.toColor(): Color = Color(this)