package ru.alexey.ndimmatrix.generator.presentation.impl.handlers

import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Single
import ru.alexey.ndimmatrix.generator.presentation.api.handlers.CreateParameterHandler
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.ParametersHolder

@Single(binds = [CreateParameterHandler::class])
class CreateParameterHandlerImpl : CreateParameterHandler(Dispatchers.Default) {
    override suspend fun handle(e: CreateParameterEvent) {
        if (e.name.isNotBlank()) {
            returnEvent {
                ParametersHolder.UpdateParameters.AddNode(
                    name = e.name,
                    description = e.description,
                    args = e.args,
                )
            }
        }
    }
}