package ru.alexey.ndimmatrix.generator.presentation.impl.handlers

import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Factory
import ru.alexey.ndimmatrix.generator.presentation.api.chains.CreateParamEventChain
import ru.alexey.ndimmatrix.generator.presentation.api.handlers.CreateParameterNameHandler
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.CreateParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.ParametersHolder

@Factory
class SetParameterNameHandlerImpl : CreateParameterNameHandler(
    dispatcher = Dispatchers.Main
) {
    override suspend fun handle(e: CreateParamEventChain.CreateParamEvent.CreateParameter) {
        returnEvent {
            ParametersHolder.UpdateParameters.AddNode(
                name = e.name,
                description = e.description,
                args = e.args,
            )
        }
    }
}