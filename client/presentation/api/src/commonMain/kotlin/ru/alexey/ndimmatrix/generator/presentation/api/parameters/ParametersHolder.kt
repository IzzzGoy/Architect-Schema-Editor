package ru.alexey.ndimmatrix.generator.presentation.api.parameters

import com.ndmatrix.core.event.Message
import com.ndmatrix.core.parameter.ParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.ParameterModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.ParametersModel

abstract class ParametersHolder(
    default: ParametersModel,
) : ParameterHolder<ParametersHolder.UpdateParameters, ParametersModel>(
    messageType = UpdateParameters::class,
    initialValue = default,
) {
    sealed interface UpdateParameters : Message.Intent {
        data class AddEdge(val from: String, val to: String) : UpdateParameters
        data class RemoveEdge(val from: String, val to: String) : UpdateParameters
        data class SetEdges(val edges: Set<Pair<String, String>>) : UpdateParameters
        data class SetNodes(val nodes: List<ParameterModel> = emptyList()) : UpdateParameters

        data class AddNode(
            val name: String,
            val description: String,
            val args: List<ArgumentModel>,
        ) : UpdateParameters

        data class DeleteNode(val name: String) : UpdateParameters
    }
}