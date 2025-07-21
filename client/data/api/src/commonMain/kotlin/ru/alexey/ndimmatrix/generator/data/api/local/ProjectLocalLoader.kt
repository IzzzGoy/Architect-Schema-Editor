package ru.alexey.ndimmatrix.generator.data.api.local

import ru.alexey.ndimmatrix.generator.data.api.models.ProjectDataModel

fun interface ProjectLocalLoader {
    suspend fun load(path: String): ProjectDataModel
}