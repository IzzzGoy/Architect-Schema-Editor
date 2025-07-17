package ru.alexey.ndimmatrix.generator.presentation.api.parameters

import com.ndmatrix.core.event.Message
import com.ndmatrix.core.parameter.ParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentType
import kotlin.reflect.KClass

sealed interface CreationArgumentIntents: Message.Intent {
    data class SetArgumentName(val name: String): CreationArgumentIntents
    data class SetArgumentNullable(val isNullable: Boolean): CreationArgumentIntents
    data class SetArgumentType(val type: ArgumentType): CreationArgumentIntents
    data object ClearArgument: CreationArgumentIntents
}

abstract class CreationArgumentParameterHolder : ParameterHolder<CreationArgumentIntents, ArgumentModel>(
    messageType = CreationArgumentIntents::class,
    initialValue = ArgumentModel(),
) {

}