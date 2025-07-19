package ru.alexey.ndimmatrix.generator.data.api.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface ProjectsModel {
    val path: String
    val name: String

    @Serializable
    data class LocalProject(
        override val path: String,
        override val name: String,
    ) : ProjectsModel
    @Serializable
    data class RemoteProject(
        override val path: String,
        override val name: String,
    ) : ProjectsModel
}