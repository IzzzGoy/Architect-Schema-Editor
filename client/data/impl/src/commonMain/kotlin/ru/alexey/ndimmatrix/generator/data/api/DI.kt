package ru.alexey.ndimmatrix.generator.data.api

import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.alexey.ndimmatrix.generator.data.api.local.ProjectLocalLoader
import ru.alexey.ndimmatrix.generator.data.api.local.ProjectLocalLoaderImpl
import ru.alexey.ndimmatrix.generator.data.api.local.ProjectLocalSaver
import ru.alexey.ndimmatrix.generator.data.api.local.ProjectLocalSaverImpl
import ru.alexey.ndimmatrix.generator.data.api.local.ProjectsDataStore
import ru.alexey.ndimmatrix.generator.data.api.local.ProjectsDataStoreImpl

internal expect fun diModule(): Module

val dataDiModule = module {
    includes(diModule())
    factory { ProjectsDataStoreImpl(get<Json>(), get<LocalStoragePathProvider>()) }.bind<ProjectsDataStore>()
    factory {
        Json {
            isLenient = true
            prettyPrint = true
        }
    }
    factoryOf(::ProjectLocalSaverImpl).bind<ProjectLocalSaver>()
    factoryOf(::ProjectLocalLoaderImpl).bind<ProjectLocalLoader>()
}