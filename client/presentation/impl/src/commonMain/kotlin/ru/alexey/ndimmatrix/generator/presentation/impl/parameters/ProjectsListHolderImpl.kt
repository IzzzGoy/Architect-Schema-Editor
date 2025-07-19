package ru.alexey.ndimmatrix.generator.presentation.impl.parameters

import org.koin.core.annotation.Single
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.ProjectsListHolder

@Single(binds = [ProjectsListHolder::class])
class ProjectsListHolderImpl: ProjectsListHolder(
    initialValue = emptySet()
) {
    override suspend fun handle(e: ProjectsListEvents) {
        when (e) {
            is ProjectsListEvents.SetProjectsList -> {
                update(e.data)
            }
        }
    }
}