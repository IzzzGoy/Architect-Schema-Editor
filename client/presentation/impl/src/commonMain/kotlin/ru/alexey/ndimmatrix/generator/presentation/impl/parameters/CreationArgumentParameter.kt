package ru.alexey.ndimmatrix.generator.presentation.impl.parameters

import arrow.optics.copy
import org.koin.core.annotation.Single
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.name
import ru.alexey.ndimmatrix.generator.presentation.api.models.nullable
import ru.alexey.ndimmatrix.generator.presentation.api.models.type
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.CreationArgumentIntents
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.CreationArgumentParameterHolder
import kotlin.uuid.ExperimentalUuidApi

@Single(binds = [CreationArgumentParameterHolder::class])
class CreationArgumentParameterHolderImpl: CreationArgumentParameterHolder() {
    override suspend fun handle(e: CreationArgumentIntents) {
        when(e) {
            is CreationArgumentIntents.ClearArgument -> {
                update(ArgumentModel())
            }
            is CreationArgumentIntents.SetArgumentName -> {
                update(
                    value.copy {
                        ArgumentModel.name set e.name
                    }
                )
            }
            is CreationArgumentIntents.SetArgumentNullable -> {
                update(
                    value.copy {
                        ArgumentModel.nullable set e.isNullable
                    }
                )
            }
            is CreationArgumentIntents.SetArgumentType -> {
                update(
                    value.copy {
                        ArgumentModel.type set e.type
                    }
                )
            }
        }
    }
}