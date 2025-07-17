package ru.alexey.ndimmatrix.generator.presentation.api.chains

import com.ndmatrix.core.event.EventChain
import com.ndmatrix.core.metadata.PostExecMetadata
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Single
import ru.alexey.ndimmatrix.generator.presentation.api.handlers.CreateParameterNameHandler
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.CreateParameterHolder

@Single
class CreateParameterModelChain(
    parameterHolder: CreateParameterHolder,
    createParameterNameHandler: CreateParameterNameHandler,
): EventChain<CreateParamEventChain.CreateParamEvent.SetParameterName>(
    coroutineContext = Dispatchers.Main.immediate,
    eventsSender = listOf(createParameterNameHandler),
    intentsHandlers = listOf(parameterHolder),
    isDebug = true,
) {
    override fun postMiddleware(postExecMetadata: PostExecMetadata<*>) {
        super.postMiddleware(postExecMetadata)
        println("SetParameterNameChain: $postExecMetadata")
    }
}