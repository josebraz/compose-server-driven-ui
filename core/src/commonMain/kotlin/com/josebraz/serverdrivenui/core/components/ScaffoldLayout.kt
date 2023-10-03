package com.josebraz.serverdrivenui.core.components

import com.josebraz.serverdrivenui.core.modifier.Modifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Scaffold")
data class ScaffoldLayout(
    val topbar: TopBarComponent = TopBarComponent(),
    override val modifier: Modifier = Modifier,
    override val children: List<Component> = emptyList()
): Layout

fun ComponentScope.Scaffold(
    modifier: Modifier = Modifier,
    topBar:  (BoxScope.() -> TopBarComponent)? = null,
    bodyBuilder: BoxScope.() -> Unit
): ScaffoldLayout {
    val scope = object : BoxScope {
        override val children: MutableList<Component> = mutableListOf()
    }
    with(scope) { bodyBuilder() }
    return ScaffoldLayout(
        topbar = topBar?.invoke(scope) ?: TopBarComponent(),
        children = scope.children,
        modifier = modifier
    ).also { this.children.add(it) }
}

@Serializable
@SerialName("Topbar")
data class TopBarComponent(
    override val modifier: Modifier = Modifier,
    override val children: List<Component> = emptyList()
): Layout


fun TopBar(
    modifier: Modifier = Modifier,
    bodyBuilder: RowScope.() -> Unit
): TopBarComponent {
    val scope = object : RowScope {
        override val children: MutableList<Component> = mutableListOf()
    }
    with(scope) { bodyBuilder() }
    return TopBarComponent(
        children = scope.children,
        modifier = modifier
    )
}