package ru.alexey.ndimmatrix.generator.presentation.api.handlers

import com.ndmatrix.core.event.AbstractEventHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.alexey.ndimmatrix.generator.presentation.api.chains.CreateParamEventChain

@OptIn(ExperimentalCoroutinesApi::class)
abstract class CreateParameterNameHandler(
    dispatcher: CoroutineDispatcher,
) : AbstractEventHandler<CreateParamEventChain.CreateParamEvent.CreateParameter>(
    messageType = CreateParamEventChain.CreateParamEvent.CreateParameter::class,
    coroutineContext = dispatcher.limitedParallelism(1),
)