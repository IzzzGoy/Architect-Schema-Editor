package ru.alexey.ndimmatrix.generator.presentation.api.parameters

import com.ndmatrix.core.event.Message
import com.ndmatrix.core.parameter.ParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.CreateParameterModel

abstract class CreateParameterHolder(
    initialValue: CreateParameterModel = CreateParameterModel()
) : ParameterHolder<CreateParameterHolder.CreateParameterIntents, CreateParameterModel>(
    messageType = CreateParameterIntents::class,
    initialValue = initialValue
) {
    sealed interface CreateParameterIntents: Message.Intent {
        data class SetParameterName(val name: String): CreateParameterIntents
        data class SetParameterDescription(val description: String): CreateParameterIntents
        data class AddArgument(val argumentModel: ArgumentModel): CreateParameterIntents
        data class RemoveArgument(val id: Int): CreateParameterIntents
        data object Clear: CreateParameterIntents
    }
}