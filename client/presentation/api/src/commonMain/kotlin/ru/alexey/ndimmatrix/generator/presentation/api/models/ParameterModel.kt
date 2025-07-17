package ru.alexey.ndimmatrix.generator.presentation.api.models

import arrow.optics.optics

@optics
data class ParametersModel(
    val edges: Set<Pair<String, String>> = emptySet(),
    val nodes: List<ParameterModel> = emptyList(),
) { companion object }

@optics
data class ParameterModel(
    val name: String,
    val description: String,
    val args: List<ArgumentModel> = emptyList(),
) { companion object }