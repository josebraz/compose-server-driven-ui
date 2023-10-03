package com.josebraz.serverdrivenui.core.components

import com.josebraz.serverdrivenui.core.action.Action
import com.josebraz.serverdrivenui.core.model.Color
import com.josebraz.serverdrivenui.core.model.Size
import com.josebraz.serverdrivenui.core.modifier.Modifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("View.Button")
data class ButtonView(
    val action: Action = Action.None,
    override val modifier: Modifier = Modifier,
    val body: List<Component> = emptyList(),
): View

fun ComponentScope.Button(
    action: Action = Action.None,
    modifier: Modifier = Modifier,
    bodyBuilder: RowScope.() -> Unit
) {
    val scope = object : RowScope {
        override val children: MutableList<Component> = mutableListOf()
    }
    with(scope) { bodyBuilder() }
    ButtonView(
        action = action,
        modifier = modifier,
        body = scope.children
    ).also { this.children.add(it) }
}