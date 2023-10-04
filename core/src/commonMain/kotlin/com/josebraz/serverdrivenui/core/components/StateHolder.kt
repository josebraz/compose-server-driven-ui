package com.josebraz.serverdrivenui.core.components

import androidx.compose.runtime.Immutable
import com.josebraz.serverdrivenui.core.model.AnyValue
import com.josebraz.serverdrivenui.core.modifier.Modifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("StateHolder")
@Immutable
data class StateHolder(
    val states: Map<String, AnyValue> = emptyMap(),
    override val modifier: Modifier = Modifier,
    override val children: List<Component> = emptyList()
): Layout

fun StateHolder(
    states: Map<String, AnyValue>,
    modifier: Modifier = Modifier,
    children: BoxScope.() -> Unit
): StateHolder {
    val scope = object : BoxScope {
        override val children: MutableList<Component> = mutableListOf()
    }
    with(scope) { children() }
    return StateHolder(
        states = states,
        modifier = modifier,
        children = scope.children
    )
}