package com.josebraz.serverdrivenui.core.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
enum class HorizontalArrangement {
    Start,
    End,
    Center,
    SpaceEvenly,
    SpaceBetween,
    SpaceAround
}

@Serializable
@Immutable
enum class VerticalArrangement {
    Top,
    Bottom,
    Center,
    SpaceEvenly,
    SpaceBetween,
    SpaceAround
}