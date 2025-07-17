package ru.alexey.ndimmatrix.generator.presentation.api.models

data class IntentModel(
    val name: String = "NewIntent",
    val args: List<ArgumentModel> = emptyList()
)