package ru.alexey.ndimmatrix.generator.presentation.impl.handlers

import org.koin.core.annotation.Single
import ru.alexey.ndimmatrix.generator.presentation.api.handlers.SaveArgumentEventHandler
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.CreationArgumentIntents
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsIntents

@Single([SaveArgumentEventHandler::class])
class SaveArgumentEventHandlerImpl : SaveArgumentEventHandler() {
    override suspend fun handle(e: SaveArgumentEvent) {
        returnEvent {
            DialogsIntents.RemoveDialog("ArgumentsCreations")
        }
        returnEvent {
            CreationArgumentIntents.ClearArgument
        }
        println(e)
    }
}