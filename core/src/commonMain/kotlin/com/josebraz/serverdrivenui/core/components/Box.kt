package com.josebraz.serverdrivenui.core.components

import com.josebraz.serverdrivenui.core.modifier.Modifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Layout.Box")
data class BoxLayout(
    override val modifier: Modifier = Modifier,
    override val children: List<Component> = emptyList()
): Layout

interface BoxScope: ComponentScope

fun ComponentScope.Box(
    modifier: Modifier = Modifier,
    bodyBuilder: BoxScope.() -> Unit
) {
    val scope = object : BoxScope {
        override val children: MutableList<Component> = mutableListOf()
    }
    with(scope) { bodyBuilder() }
    BoxLayout(
        children = scope.children,
        modifier = modifier
    ).also { this.children.add(it) }
}

fun ComponentScope.Box(
    modifier: Modifier = Modifier
) {
    BoxLayout(
        modifier = modifier
    ).also { this.children.add(it) }
}

