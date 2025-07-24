package ru.alexey.ndimmatrix.generator.presentation.api.models

import arrow.optics.optics

@optics
data class EventModel(
    val name: String = "",
    val description: String = "",
    val args: List<ArgumentModel> = emptyList(),
    val parameter: String? = null,
) { companion object }


@optics
data class EventsModel(
    val edges: Set<Pair<String, String>> = emptySet(),
    val nodes: List<EventModel> = emptyList(),
) { companion object }