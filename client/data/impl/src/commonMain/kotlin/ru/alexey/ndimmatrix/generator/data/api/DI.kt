package ru.alexey.ndimmatrix.generator.data.api

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.alexey.ndimmatrix.generator.data.api.local.ProjectsDataStore
import ru.alexey.ndimmatrix.generator.data.api.local.ProjectsDataStoreImpl

internal expect fun diModule(): Module

val dataDiModule = module {
    includes(diModule())
    factoryOf(::ProjectsDataStoreImpl).bind<ProjectsDataStore>()
}