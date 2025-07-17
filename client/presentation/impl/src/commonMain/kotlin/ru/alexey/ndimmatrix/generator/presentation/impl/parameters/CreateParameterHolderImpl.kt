package ru.alexey.ndimmatrix.generator.presentation.impl.parameters

import arrow.optics.copy
import org.koin.core.annotation.Single
import ru.alexey.ndimmatrix.generator.presentation.api.models.CreateParameterModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.args
import ru.alexey.ndimmatrix.generator.presentation.api.models.description
import ru.alexey.ndimmatrix.generator.presentation.api.models.name
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.CreateParameterHolder

@Single(binds = [CreateParameterHolder::class])
class CreateParameterHolderImpl : CreateParameterHolder(
    initialValue = CreateParameterModel()
) {
    override suspend fun handle(e: CreateParameterIntents) {
        when (e) {
            is CreateParameterIntents.SetParameterDescription -> {
                update(
                    value = value.copy {
                        CreateParameterModel.description set e.description
                    }
                )
            }
            is CreateParameterIntents.SetParameterName -> {
                update(
                    value = value.copy {
                        CreateParameterModel.name set e.name
                    }
                )
            }

            is CreateParameterIntents.Clear -> {
                update(
                    value = CreateParameterModel()
                )
            }

            is CreateParameterIntents.AddArgument -> {
                update(
                    value = value.copy {
                        CreateParameterModel.args set value.args + e.argumentModel
                    }
                )
            }
            is CreateParameterIntents.RemoveArgument -> {
                update(
                    value = value.copy {
                        CreateParameterModel.args set value.args.filter { it.id != e.id }
                    }
                )
            }
        }
    }
}