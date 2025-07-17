package ru.alexey.ndimmatrix.generator.ui.chains

import com.ndmatrix.core.event.EventChain
import com.ndmatrix.core.metadata.PostExecMetadata
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Factory
import ru.alexey.ndimmatrix.generator.presentation.api.handlers.SaveArgumentEventHandler
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.CreationArgumentParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsParameterHolder

@Factory
class SaveArgumentChain(
    dialogsParameterHolder: DialogsParameterHolder,
    creationArgumentParameterHolder: CreationArgumentParameterHolder,
    saveArgumentEventHandler: SaveArgumentEventHandler,
) : EventChain<SaveArgumentEventHandler.SaveArgumentEvent>(
    coroutineContext = Dispatchers.Default,
    isDebug = true,
    intentsHandlers = listOf(
        dialogsParameterHolder,
        creationArgumentParameterHolder,
    ),
    eventsSender = listOf(
        saveArgumentEventHandler
    )
) {

    override fun postMiddleware(postExecMetadata: PostExecMetadata<*>) {
        super.postMiddleware(postExecMetadata)
        println(postExecMetadata)
    }

}