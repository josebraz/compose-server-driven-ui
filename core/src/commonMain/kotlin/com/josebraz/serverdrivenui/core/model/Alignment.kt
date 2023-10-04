package com.josebraz.serverdrivenui.core.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
enum class Alignment {
    TopStart,
    TopCenter,
    TopEnd,
    CenterStart,
    Center,
    CenterEnd,
    BottomStart,
    BottomCenter,
    BottomEnd
}

@Serializable
@Immutable
enum class VerticalAlignment {
    Top,
    CenterVertically,
    Bottom
}

@Serializable
@Immutable
enum class HorizontalAlignment {
    Start,
    CenterHorizontally,
    End
}