package ru.alexey.ndimmatrix.generator.data.api.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString
import kotlinx.serialization.json.Json
import ru.alexey.ndimmatrix.generator.data.api.models.ProjectDataModel

class ProjectLocalSaverImpl(
    private val json: Json,
) : ProjectLocalSaver {
    override suspend fun save(path: String, data: ProjectDataModel) {
        withContext(Dispatchers.IO) {
            SystemFileSystem
                .sink(Path(path))
                .buffered()
                .use {
                    it.writeString(
                        json.encodeToString(data)
                    )
                    it.flush()
                }
        }
    }
}