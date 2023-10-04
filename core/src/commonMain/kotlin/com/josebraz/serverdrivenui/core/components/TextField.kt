package com.josebraz.serverdrivenui.core.components

import androidx.compose.runtime.Immutable
import com.josebraz.serverdrivenui.core.action.Action
import com.josebraz.serverdrivenui.core.modifier.Modifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("View.TextField")
@Immutable
data class TextFieldView(
    val text: String,
    val onChange: String,
    override val modifier: Modifier = Modifier,
): View

fun ComponentScope.TextField(
    text: String,
    onChange: String,
    modifier: Modifier = Modifier,
) {
    TextFieldView(
        text = text,
        onChange = onChange,
        modifier = modifier
    ).also { this.children.add(it) }
}
