package ru.alexey.ndimmatrix.generator.presentation.api.parameters

import com.ndmatrix.core.event.Message
import com.ndmatrix.core.parameter.ParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.EventModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.EventsModel

abstract class EventsHolder(
    initialValue: EventsModel
) : ParameterHolder<EventsHolder.EventHolderIntents, EventsModel>(
    initialValue = initialValue,
    messageType = EventHolderIntents::class
) {

    sealed interface EventHolderIntents : Message.Intent {
        data class AddNode(
            val name: String,
            val description: String,
            val args: List<ArgumentModel>,
            val parameter: String? = null,
        ) : EventHolderIntents

        data class DeleteNode(val name: String) : EventHolderIntents
        data class AddEdge(val from: String, val to: String) : EventHolderIntents
        data class RemoveEdge(val from: String, val to: String) : EventHolderIntents
        data class SetEdges(val edges: Set<Pair<String, String>>) : EventHolderIntents
        data class SetNodes(val nodes: List<EventModel> = emptyList()) : EventHolderIntents
    }
}