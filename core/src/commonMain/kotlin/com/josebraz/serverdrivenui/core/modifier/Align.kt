package com.josebraz.serverdrivenui.core.modifier

import androidx.compose.runtime.Immutable
import com.josebraz.serverdrivenui.core.model.Alignment
import com.josebraz.serverdrivenui.core.model.HorizontalAlignment
import com.josebraz.serverdrivenui.core.model.VerticalAlignment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Modifier.Align")
@Immutable
data class Align(
    val align: Alignment
): Element

@Serializable
@SerialName("Modifier.AlignVertical")
@Immutable
data class AlignVertical(
    val align: VerticalAlignment
): Element

@Serializable
@SerialName("Modifier.AlignHorizontal")
@Immutable
data class AlignHorizontal(
    val align: HorizontalAlignment
): Element