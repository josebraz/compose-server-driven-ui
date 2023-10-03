package com.josebraz.serverdrivenui.core.components

import com.josebraz.serverdrivenui.core.modifier.Modifier
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator


@Serializable
@SerialName("Component")
sealed interface Component {
    val modifier: Modifier get() = Modifier
}

@Serializable
@SerialName("Layout")
sealed interface Layout: Component {
    val children: List<Component> get() = emptyList()
}

@Serializable
@SerialName("View")
sealed interface View: Component

@Serializable
@SerialName("EmptyComponent")
data object EmptyComponent: Component

interface ComponentScope {
    val children: MutableList<Component> get() = mutableListOf()
}