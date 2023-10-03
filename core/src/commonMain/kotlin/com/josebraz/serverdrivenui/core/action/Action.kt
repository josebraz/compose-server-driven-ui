package com.josebraz.serverdrivenui.core.action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DialogAction(
    val title: String,
    val action: Action
)

@Serializable
@SerialName("Action")
sealed interface Action {
    @Serializable
    @SerialName("Action.None")
    data object None: Action
    
    @Serializable
    @SerialName("Action.Execute")
    data class Execute(
        val command: String
    ): Action

    @Serializable
    @SerialName("Action.Navigate")
    data class Navigate(
        val path: String,
        val params: Map<String, String> = emptyMap()
    ): Action

    @Serializable
    @SerialName("Action.ShowDialog")
    data class ShowDialog(
        val title: String = "",
        val description: String = "",
        val actions: List<DialogAction> = emptyList()
    ): Action

    @Serializable
    @SerialName("Action.CloseDialog")
    data object CloseDialog: Action

    @Serializable
    @SerialName("Action.PostRequest")
    sealed interface Request: Action {
        val path: String
        val body: String
        val onSuccess: Action get() = None
        val onError: Action get() = None
        val headers: Map<String, String> get() = emptyMap()
        @Serializable
        @SerialName("Action.Request.Post")
        data class Post(
            override val path: String,
            override val body: String,
            override val onSuccess: Action = None,
            override val onError: Action = None,
            override val headers: Map<String, String> = emptyMap(),
        ): Request
    }
}
