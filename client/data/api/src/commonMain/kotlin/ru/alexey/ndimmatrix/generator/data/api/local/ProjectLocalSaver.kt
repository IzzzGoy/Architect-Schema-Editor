package ru.alexey.ndimmatrix.generator.data.api.local

import ru.alexey.ndimmatrix.generator.data.api.models.ProjectDataModel

fun interface ProjectLocalSaver {
    suspend fun save(path: String, data: ProjectDataModel)
}