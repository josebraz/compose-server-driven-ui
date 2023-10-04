package com.josebraz.serverdrivenui.core.components

import androidx.compose.runtime.Immutable
import com.josebraz.serverdrivenui.core.model.HorizontalAlignment
import com.josebraz.serverdrivenui.core.model.VerticalAlignment
import com.josebraz.serverdrivenui.core.model.VerticalArrangement
import com.josebraz.serverdrivenui.core.modifier.AlignHorizontal
import com.josebraz.serverdrivenui.core.modifier.AlignVertical
import com.josebraz.serverdrivenui.core.modifier.Modifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Layout.Column")
@Immutable
data class ColumnLayout(
    override val modifier: Modifier = Modifier,
    val verticalArrangement: VerticalArrangement = VerticalArrangement.Top,
    val horizontalAlignment: HorizontalAlignment = HorizontalAlignment.Start,
    override val children: List<Component> = emptyList()
): Layout

interface ColumnScope: ComponentScope {
    fun Modifier.align(alignment: HorizontalAlignment): Modifier {
        return AlignHorizontal(alignment).then(this)
    }
}

fun ComponentScope.Column(
    modifier: Modifier = Modifier,
    verticalArrangement: VerticalArrangement = VerticalArrangement.Top,
    horizontalAlignment: HorizontalAlignment = HorizontalAlignment.Start,
    bodyBuilder: ColumnScope.() -> Unit
) {
    val scope = object : ColumnScope {
        override val children: MutableList<Component> = mutableListOf()
    }
    with(scope) { bodyBuilder() }
    ColumnLayout(
        children = scope.children,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ).also { this.children.add(it) }
}