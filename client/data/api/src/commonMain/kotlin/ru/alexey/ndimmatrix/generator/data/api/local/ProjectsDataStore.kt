package ru.alexey.ndimmatrix.generator.data.api.local

import ru.alexey.ndimmatrix.generator.data.api.Storage
import ru.alexey.ndimmatrix.generator.data.api.models.ProjectsModel

interface ProjectsDataStore: Storage<Set<ProjectsModel>>