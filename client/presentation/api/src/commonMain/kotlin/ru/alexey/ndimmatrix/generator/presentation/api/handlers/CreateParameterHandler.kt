package ru.alexey.ndimmatrix.generator.presentation.api.handlers

import com.ndmatrix.core.event.AbstractEventHandler
import com.ndmatrix.core.event.Message
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel

@OptIn(ExperimentalCoroutinesApi::class)
abstract class CreateParameterHandler(
    dispatcher: CoroutineDispatcher,
) : AbstractEventHandler<CreateParameterHandler.CreateParameterEvent>(
    messageType = CreateParameterEvent::class,
    coroutineContext = dispatcher.limitedParallelism(1)
) {
    data class CreateParameterEvent(
        val name: String,
        val description: String,
        val args: List<ArgumentModel>,
    ) : Message.Event
}