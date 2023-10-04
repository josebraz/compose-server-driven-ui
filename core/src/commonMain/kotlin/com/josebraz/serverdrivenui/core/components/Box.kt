package com.josebraz.serverdrivenui.core.components

import androidx.compose.runtime.Immutable
import com.josebraz.serverdrivenui.core.model.Alignment
import com.josebraz.serverdrivenui.core.modifier.Align
import com.josebraz.serverdrivenui.core.modifier.Modifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Layout.Box")
@Immutable
data class BoxLayout(
    override val modifier: Modifier = Modifier,
    val contentAlignment: Alignment = Alignment.TopStart,
    override val children: List<Component> = emptyList()
): Layout

interface BoxScope: ComponentScope {
    fun Modifier.align(alignment: Alignment): Modifier {
        return Align(alignment).then(this)
    }
}

fun ComponentScope.Box(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    bodyBuilder: BoxScope.() -> Unit
) {
    val scope = object : BoxScope {
        override val children: MutableList<Component> = mutableListOf()
    }
    with(scope) { bodyBuilder() }
    BoxLayout(
        children = scope.children,
        contentAlignment = contentAlignment,
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

