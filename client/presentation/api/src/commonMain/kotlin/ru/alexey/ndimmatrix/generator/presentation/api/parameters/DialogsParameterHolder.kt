package ru.alexey.ndimmatrix.generator.presentation.api.parameters

import androidx.compose.runtime.Composable
import com.ndmatrix.core.event.Message
import com.ndmatrix.core.parameter.ParameterHolder


interface DialogWrapper {
    @Composable
    fun Content()
}

sealed interface DialogsIntents: Message.Intent {
    data class AddDialog(val key: String, val dialog: DialogWrapper): DialogsIntents
    data class RemoveDialog(val key: String): DialogsIntents
}

abstract class DialogsParameterHolder: ParameterHolder<DialogsIntents, Map<String, DialogWrapper>>(
    initialValue = emptyMap(),
    messageType = DialogsIntents::class
)