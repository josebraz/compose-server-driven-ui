package com.josebraz.serverdrivenui.core.components

import com.josebraz.serverdrivenui.core.modifier.Modifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Layout.Row")
data class RowLayout(
    override val modifier: Modifier = Modifier,
    override val children: List<Component> = emptyList()
): Layout

interface RowScope: ComponentScope

fun ComponentScope.Row(
    modifier: Modifier = Modifier,
    bodyBuilder: RowScope.() -> Unit
) {
    val scope = object : RowScope {
        override val children: MutableList<Component> = mutableListOf()
    }
    with(scope) { bodyBuilder() }
    RowLayout(
        children = scope.children,
        modifier = modifier
    ).also { this.children.add(it) }
}