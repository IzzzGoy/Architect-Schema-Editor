package ru.alexey.ndimmatrix.generator.presentation.api.models

import arrow.optics.optics

@optics
data class CreateParameterModel(
    val name: String = "",
    val description: String = "",
    val args: List<ArgumentModel> = emptyList()
) { companion object }
