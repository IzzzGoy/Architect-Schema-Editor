package ru.alexey.ndimmatrix.generator.presentation.impl.parameters

import org.koin.core.annotation.Single
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsIntents
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsParameterHolder
import kotlin.reflect.KClass

@Single
class DialogsParameterHolderImpl: DialogsParameterHolder() {
    
    override suspend fun handle(e: DialogsIntents) {
        when(e) {
            is DialogsIntents.AddDialog -> {
                update(
                    value = value + (e.key to e.dialog)
                )
            }
            is DialogsIntents.RemoveDialog -> {
                update(
                    value = value - e.key
                )
            }
        }
    }
}