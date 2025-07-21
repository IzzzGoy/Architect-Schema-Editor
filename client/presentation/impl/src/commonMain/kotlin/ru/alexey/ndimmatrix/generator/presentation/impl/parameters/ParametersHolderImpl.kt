package ru.alexey.ndimmatrix.generator.presentation.impl.parameters

import arrow.optics.copy
import org.koin.core.annotation.Single
import ru.alexey.ndimmatrix.generator.presentation.api.models.ParameterModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.ParametersModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.edges
import ru.alexey.ndimmatrix.generator.presentation.api.models.nodes
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.ParametersHolder

@Single(binds = [ParametersHolder::class])
class ParametersHolderImpl: ParametersHolder(ParametersModel()) {
    override suspend fun handle(e: UpdateParameters) {
        when (e) {
            is UpdateParameters.AddEdge -> update(
                value.copy {
                    ParametersModel.edges set value.edges + (e.from to e.to)
                }
            )
            is UpdateParameters.AddNode -> update(
                value.copy {
                    ParametersModel.nodes set value.nodes + ParameterModel(
                        name = e.name,
                        description = e.description,
                        args = e.args,
                    )
                }
            )
            is UpdateParameters.DeleteNode -> update(
                value.copy {
                    ParametersModel.nodes set value.nodes.filter { node ->
                        node.name != e.name
                    }
                }
            )
            is UpdateParameters.RemoveEdge -> update(
                value.copy {
                    ParametersModel.edges set value.edges.filter { (from, to) ->
                        from != e.from && to != e.to
                    }.toSet()
                }
            )
            is UpdateParameters.SetEdges -> update(
                value.copy {
                    ParametersModel.edges set e.edges
                }
            )

            is UpdateParameters.SetNodes -> update(
                value.copy {
                    ParametersModel.nodes set e.nodes
                }
            )
        }
    }
}