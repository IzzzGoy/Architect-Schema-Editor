package ru.alexey.ndimmatrix.generator.presentation.impl.parameters

import arrow.optics.copy
import org.koin.core.annotation.Single
import ru.alexey.ndimmatrix.generator.presentation.api.models.EventModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.EventsModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.edges
import ru.alexey.ndimmatrix.generator.presentation.api.models.nodes
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.EventsHolder

@Single(binds = [EventsHolder::class])
class EventsHolderImpl : EventsHolder(
    EventsModel()
) {

    override suspend fun handle(e: EventHolderIntents) {
        when (e) {
            is EventHolderIntents.AddEdge -> update(
                value.copy {
                    EventsModel.edges set value.edges + (e.from to e.to)
                }
            )
            is EventHolderIntents.AddNode -> update(
                value.copy {
                    EventsModel.nodes set value.nodes + EventModel(
                        name = e.name,
                        description = e.description,
                        args = e.args,
                        parameter = e.parameter,
                    )
                }
            )
            is EventHolderIntents.DeleteNode -> update(
                value.copy {
                    EventsModel.nodes set value.nodes.filter { node ->
                        node.name != e.name
                    }
                }
            )
            is EventHolderIntents.RemoveEdge -> update(
                value.copy {
                    EventsModel.edges set value.edges.filter { (from, to) ->
                        from != e.from && to != e.to
                    }.toSet()
                }
            )
            is EventHolderIntents.SetEdges -> update(
                value.copy {
                    EventsModel.edges set e.edges
                }
            )

            is EventHolderIntents.SetNodes -> update(
                value.copy {
                    EventsModel.nodes set e.nodes
                }
            )
        }
    }
}