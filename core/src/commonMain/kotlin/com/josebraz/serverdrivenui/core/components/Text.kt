package com.josebraz.serverdrivenui.core.components

import androidx.compose.runtime.Immutable
import com.josebraz.serverdrivenui.core.model.Color
import com.josebraz.serverdrivenui.core.model.Size
import com.josebraz.serverdrivenui.core.modifier.Modifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("View.Text")
@Immutable
data class TextView(
    val text: String,
    override val modifier: Modifier = Modifier,
    val textSize: Size = Size.Undefined,
    val textColor: Color = Color.Undefined
): View

fun ComponentScope.Text(
    text: String,
    modifier: Modifier = Modifier,
    textSize: Size = Size.Undefined,
    textColor: Color = Color.Undefined
) {
    TextView(
        text = text,
        modifier = modifier,
        textSize = textSize,
        textColor = textColor
    ).also { this.children.add(it) }
}