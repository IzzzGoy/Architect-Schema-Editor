package ru.alexey.ndimmatrix.generator.presentation.api.chains

import com.ndmatrix.core.event.EventChain
import com.ndmatrix.core.event.Message
import kotlinx.coroutines.Dispatchers
import ru.alexey.ndimmatrix.generator.presentation.api.handlers.CreateParameterNameHandler
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.CreateParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.ParametersHolder
import kotlin.coroutines.CoroutineContext


class CreateParamEventChain(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    isDebug: Boolean = true,
    createParameterNameHandler: CreateParameterNameHandler,
    parameterHolder: CreateParameterHolder,
    parametersHolder: ParametersHolder,
) : EventChain<CreateParamEventChain.CreateParamEvent>(
    coroutineContext = coroutineContext,
    eventsSender = listOf(createParameterNameHandler),
    intentsHandlers = listOf(parameterHolder, parametersHolder),
    isDebug = isDebug,
) {
    sealed interface CreateParamEvent : Message.Event {
        data class SetParameterName(val name: String) : CreateParamEvent
        data class SetParameterDescription(val description: String) : CreateParamEvent
        data class CreateParameter(
            val name: String,
            val description: String,
            val args: List<ArgumentModel>,
        ) : CreateParamEvent
    }
}