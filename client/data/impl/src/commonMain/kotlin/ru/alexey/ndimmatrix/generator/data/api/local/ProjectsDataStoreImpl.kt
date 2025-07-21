package ru.alexey.ndimmatrix.generator.data.api.local


import io.github.xxfast.kstore.file.storeOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.json.Json
import ru.alexey.ndimmatrix.generator.data.api.LocalStoragePathProvider
import ru.alexey.ndimmatrix.generator.data.api.models.ProjectsModel

class ProjectsDataStoreImpl(
    json: Json,
    pathProvider: LocalStoragePathProvider,
): ProjectsDataStore {

    init {
        SystemFileSystem.createDirectories(Path(pathProvider.provide()))
    }

    private val kStore = storeOf<Set<ProjectsModel>>(
        file = Path(pathProvider.provide() + "/projects.json"),
        json = json,
        default = emptySet()
    )

    override val data: Flow<Set<ProjectsModel>> = kStore.updates.filterNotNull()

    override suspend fun update(data: Set<ProjectsModel>) = withContext(Dispatchers.IO.limitedParallelism(1)) {
        kStore.update {
            data
        }
    }
}