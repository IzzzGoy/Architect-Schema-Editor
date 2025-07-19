package ru.alexey.ndimmatrix.generator.presentation.api.parameters

import com.ndmatrix.core.event.Message
import com.ndmatrix.core.parameter.ParameterHolder
import ru.alexey.ndimmatrix.generator.data.api.models.ProjectsModel

abstract class ProjectsListHolder(
    initialValue: Set<ProjectsModel>,
) : ParameterHolder<ProjectsListHolder.ProjectsListEvents, Set<ProjectsModel>>(
    messageType = ProjectsListEvents::class,
    initialValue = initialValue
) {
    sealed interface ProjectsListEvents: Message.Intent {
        data class SetProjectsList(val data: Set<ProjectsModel>): ProjectsListEvents
    }
}