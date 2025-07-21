package ru.alexey.ndimmatrix.generator.data.api.local

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.serialization.json.Json
import ru.alexey.ndimmatrix.generator.data.api.models.ProjectDataModel

class ProjectLocalLoaderImpl(
    private val json: Json
) : ProjectLocalLoader {
    override suspend fun load(path: String): ProjectDataModel {
        return runCatching {
            SystemFileSystem.source(Path(path))
                .buffered()
                .readString()
                .let {
                    json.decodeFromString<ProjectDataModel>(it)
                }
        }.getOrElse {
            ProjectDataModel()
        }
    }

}