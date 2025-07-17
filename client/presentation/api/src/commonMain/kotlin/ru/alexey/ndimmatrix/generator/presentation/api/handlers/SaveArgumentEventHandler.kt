package ru.alexey.ndimmatrix.generator.presentation.api.handlers

import com.ndmatrix.core.event.AbstractEventHandler
import com.ndmatrix.core.event.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel

@OptIn(ExperimentalCoroutinesApi::class)
abstract class SaveArgumentEventHandler : AbstractEventHandler<SaveArgumentEventHandler.SaveArgumentEvent>(
    coroutineContext = Dispatchers.Default.limitedParallelism(1),
    messageType = SaveArgumentEvent::class
) {
    data class SaveArgumentEvent(val argument: ArgumentModel): Message.Event
}