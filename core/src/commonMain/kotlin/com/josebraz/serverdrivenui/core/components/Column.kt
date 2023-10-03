package com.josebraz.serverdrivenui.core.components

import com.josebraz.serverdrivenui.core.modifier.Modifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Layout.Column")
data class ColumnLayout(
    override val modifier: Modifier = Modifier,
    override val children: List<Component> = emptyList()
): Layout

interface ColumnScope: ComponentScope

fun ComponentScope.Column(
    modifier: Modifier = Modifier,
    bodyBuilder: ColumnScope.() -> Unit
) {
    val scope = object : ColumnScope {
        override val children: MutableList<Component> = mutableListOf()
    }
    with(scope) { bodyBuilder() }
    ColumnLayout(
        children = scope.children,
        modifier = modifier
    ).also { this.children.add(it) }
}