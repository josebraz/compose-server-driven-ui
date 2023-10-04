package com.josebraz.serverdrivenui.core.components

import androidx.compose.runtime.Immutable
import com.josebraz.serverdrivenui.core.model.Alignment
import com.josebraz.serverdrivenui.core.model.HorizontalArrangement
import com.josebraz.serverdrivenui.core.model.VerticalAlignment
import com.josebraz.serverdrivenui.core.modifier.Align
import com.josebraz.serverdrivenui.core.modifier.AlignVertical
import com.josebraz.serverdrivenui.core.modifier.Modifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Layout.Row")
@Immutable
data class RowLayout(
    override val modifier: Modifier = Modifier,
    val horizontalArrangement: HorizontalArrangement = HorizontalArrangement.Start,
    val verticalAlignment: VerticalAlignment = VerticalAlignment.Top,
    override val children: List<Component> = emptyList()
): Layout

interface RowScope: ComponentScope {
    fun Modifier.align(alignment: VerticalAlignment): Modifier {
        return AlignVertical(alignment).then(this)
    }
}

fun ComponentScope.Row(
    modifier: Modifier = Modifier,
    horizontalArrangement: HorizontalArrangement = HorizontalArrangement.Start,
    verticalAlignment: VerticalAlignment = VerticalAlignment.Top,
    bodyBuilder: RowScope.() -> Unit
) {
    val scope = object : RowScope {
        override val children: MutableList<Component> = mutableListOf()
    }
    with(scope) { bodyBuilder() }
    RowLayout(
        children = scope.children,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        modifier = modifier
    ).also { this.children.add(it) }
}