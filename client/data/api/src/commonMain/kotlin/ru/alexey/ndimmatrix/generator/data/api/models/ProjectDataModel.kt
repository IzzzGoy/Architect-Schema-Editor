package ru.alexey.ndimmatrix.generator.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class ProjectDataModel(
    val parametersWorkspaces: List<ParametersDataModel> = emptyList()
)

@Serializable
data class ParametersDataModel(
    val edges: Set<Pair<String, String>> = emptySet(),
    val nodes: List<ParameterDataModel> = emptyList(),
)

@Serializable
data class ParameterDataModel(
    val name: String,
    val description: String,
    val args: List<ArgumentDataModel> = emptyList(),
)

@Serializable
data class ArgumentDataModel(
    val id: Int,
    val name: String,
    val description: String = "",
    val type: ArgumentDataType,
    val nullable: Boolean = false,
)
