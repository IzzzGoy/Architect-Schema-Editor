package ru.alexey.ndimmatrix.generator.ui.screens

import androidx.compose.ui.Alignment
import kotlinx.coroutines.CoroutineScope

/*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arrow.core.toOption
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ru.alexey.ndimmatrix.generator.data.api.local.ProjectLocalLoader
import ru.alexey.ndimmatrix.generator.data.api.local.ProjectsDataStore
import ru.alexey.ndimmatrix.generator.data.api.models.ProjectsModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentType
import ru.alexey.ndimmatrix.generator.presentation.api.models.ParameterModel
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.ParametersHolder

@Composable
fun HomeScreen(
    toParamsScreen: (project: String) -> Unit,
) {
    val projectsDataStore = koinInject<ProjectsDataStore>()
    val state by projectsDataStore.data.collectAsState(emptySet())

    val parameters = koinInject<ParametersHolder>()

    var showFilePicker by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val projectLocalLoader = koinInject<ProjectLocalLoader>()

    LazyVerticalGrid(
        columns = GridCells.FixedSize(100.dp)
    ) {
        items(state.toList()) { project ->
            Button(
                onClick = {
                    coroutineScope.launch {
                        project.toOption()
                            .map {
                                coroutineScope.launch {
                                    projectsDataStore.update(
                                        state + ProjectsModel.LocalProject(name = "test", path = it.path)
                                    )
                                }
                                toParamsScreen(it.path)
                                projectLocalLoader.load(it.path)
                            }
                            .map {
                                it.parametersWorkspaces.firstOrNull()
                                    .toOption()
                                    .onSome {
                                        coroutineScope.launch {
                                            parameters.handle(
                                                ParametersHolder.UpdateParameters.SetEdges(
                                                    it.edges
                                                )
                                            )
                                            parameters.handle(
                                                ParametersHolder.UpdateParameters.SetNodes(
                                                    it.nodes.map { param ->
                                                        ParameterModel(
                                                            param.name,
                                                            param.description,
                                                            param.args.map { arg ->
                                                                ArgumentModel(
                                                                    id = arg.id,
                                                                    name = arg.name,
                                                                    type = ArgumentType.valueOf(arg.type.toString()),
                                                                    nullable = arg.nullable
                                                                )
                                                            }
                                                        )
                                                    }
                                                )
                                            )
                                        }
                                    }

                            }
                    }
                }
            ) {
                Text(project.name)
            }
        }
        item {
            IconButton(
                onClick = {
                    showFilePicker = true
                }
            ) {
                Icon(
                    modifier = Modifier.fillMaxWidth(),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add project"
                )
            }
        }
    }

    FilePicker(show = showFilePicker, fileExtensions = listOf("json")) { platformFile ->
        showFilePicker = false

        coroutineScope.launch {
            platformFile.toOption()
                .map {
                    coroutineScope.launch {
                        projectsDataStore.update(
                            state + ProjectsModel.LocalProject(name = "test", path = it.path)
                        )
                    }
                    toParamsScreen(it.path)
                    projectLocalLoader.load(it.path)
                }
                .map {
                    it.parametersWorkspaces.firstOrNull()
                        .toOption()
                        .onSome {
                            coroutineScope.launch {
                                parameters.handle(
                                    ParametersHolder.UpdateParameters.SetEdges(
                                        it.edges
                                    )
                                )
                                parameters.handle(
                                    ParametersHolder.UpdateParameters.SetNodes(
                                        it.nodes.map { param ->
                                            ParameterModel(
                                                param.name,
                                                param.description,
                                                param.args.map { arg ->
                                                    ArgumentModel(
                                                        id = arg.id,
                                                        name = arg.name,
                                                        type = ArgumentType.valueOf(arg.type.toString()),
                                                        nullable = arg.nullable
                                                    )
                                                }
                                            )
                                        }
                                    )
                                )
                            }
                        }

                }
        }
    }
}*/

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import arrow.core.toOption
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ru.alexey.ndimmatrix.generator.data.api.local.ProjectLocalLoader
import ru.alexey.ndimmatrix.generator.data.api.local.ProjectsDataStore
import ru.alexey.ndimmatrix.generator.data.api.models.ProjectsModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentType
import ru.alexey.ndimmatrix.generator.presentation.api.models.EventModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.ParameterModel
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.EventsHolder
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.ParametersHolder

enum class ProjectType {
    LOCAL, REMOTE
}

@Composable
fun HomeScreen(
    toParamsScreen: (project: String) -> Unit,
) {
    val projectsDataStore = koinInject<ProjectsDataStore>()
    val state by projectsDataStore.data.collectAsState(emptySet())
    val parameters = koinInject<ParametersHolder>()
    val events = koinInject<EventsHolder>()
    val projectLocalLoader = koinInject<ProjectLocalLoader>()
    val coroutineScope = rememberCoroutineScope()

    // Состояния для диалогов
    var showCreateDialog by remember { mutableStateOf(false) }
    var showFilePicker by remember { mutableStateOf(false) }
    var selectedProjectType by remember { mutableStateOf(ProjectType.LOCAL) }
    var projectName by remember { mutableStateOf("") }

    // Диалог создания проекта
    if (showCreateDialog) {
        CreateProjectDialog(
            projectName = projectName,
            onNameChange = { projectName = it },
            selectedType = selectedProjectType,
            onTypeChange = { selectedProjectType = it },
            onConfirm = {
                showCreateDialog = false
                when (selectedProjectType) {
                    ProjectType.LOCAL -> showFilePicker = true
                    ProjectType.REMOTE -> createRemoteProject(
                        projectName,
                        projectsDataStore,
                        toParamsScreen,
                        coroutineScope
                    )
                }
            },
            onDismiss = { showCreateDialog = false }
        )
    }

    // Файловый пикер для локальных проектов
    if (showFilePicker) {
        FilePicker(
            show = showFilePicker,
        ) { platformFile ->
            showFilePicker = false
            platformFile.toOption().map { file ->
                val project = ProjectsModel.LocalProject(
                    name = projectName.ifEmpty { file.path.takeLastWhile { it != '/' } },
                    path = file.path
                )

                coroutineScope.launch {
                    // Сохраняем проект
                    projectsDataStore.update(state + project)

                    // Загружаем параметры
                    projectLocalLoader.load(file.path).let { workspace ->
                        workspace.parametersWorkspaces.firstOrNull()?.let {
                            parameters.handle(
                                ParametersHolder.UpdateParameters.SetEdges(it.edges)
                            )
                            parameters.handle(
                                ParametersHolder.UpdateParameters.SetNodes(
                                    it.nodes.map { param ->
                                        ParameterModel(
                                            param.name,
                                            param.description,
                                            param.args.map { arg ->
                                                ArgumentModel(
                                                    id = arg.id,
                                                    name = arg.name,
                                                    type = ArgumentType.valueOf(arg.type.toString()),
                                                    nullable = arg.nullable
                                                )
                                            }
                                        )
                                    }
                                )
                            )
                        }
                        workspace.eventsWorkspaces.firstOrNull()?.also {
                            events.handle(
                                EventsHolder.EventHolderIntents.SetEdges(it.edges)
                            )
                            events.handle(
                                EventsHolder.EventHolderIntents.SetNodes(
                                    it.nodes.map { param ->
                                        EventModel(
                                            param.name,
                                            param.description,
                                            param.args.map { arg ->
                                                ArgumentModel(
                                                    id = arg.id,
                                                    name = arg.name,
                                                    type = ArgumentType.valueOf(arg.type.toString()),
                                                    nullable = arg.nullable
                                                )
                                            },
                                            param.parameter,
                                        )
                                    }
                                )
                            )
                        }
                    }

                    // Переходим на экран параметров
                    toParamsScreen(file.path)
                }
            }
        }
    }

    // Список проектов
    LazyVerticalGrid(
        columns = GridCells.FixedSize(100.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.toList()) { project ->
            ProjectItem(
                project = project,
                onClick = {
                    coroutineScope.launch {
                        project.toOption()
                            .map {
                                coroutineScope.launch {
                                    projectsDataStore.update(
                                        state + ProjectsModel.LocalProject(name = "test", path = it.path)
                                    )
                                }
                                toParamsScreen(it.path)
                                projectLocalLoader.load(it.path)
                            }
                            .map {
                                it.parametersWorkspaces.firstOrNull()
                                    .toOption()
                                    .onSome {
                                        coroutineScope.launch {
                                            parameters.handle(
                                                ParametersHolder.UpdateParameters.SetEdges(
                                                    it.edges
                                                )
                                            )
                                            parameters.handle(
                                                ParametersHolder.UpdateParameters.SetNodes(
                                                    it.nodes.map { param ->
                                                        ParameterModel(
                                                            param.name,
                                                            param.description,
                                                            param.args.map { arg ->
                                                                ArgumentModel(
                                                                    id = arg.id,
                                                                    name = arg.name,
                                                                    type = ArgumentType.valueOf(arg.type.toString()),
                                                                    nullable = arg.nullable
                                                                )
                                                            }
                                                        )
                                                    }
                                                )
                                            )
                                        }
                                    }
                                it.eventsWorkspaces.firstOrNull()?.also {
                                    events.handle(
                                        EventsHolder.EventHolderIntents.SetEdges(it.edges)
                                    )
                                    events.handle(
                                        EventsHolder.EventHolderIntents.SetNodes(
                                            it.nodes.map { param ->
                                                EventModel(
                                                    param.name,
                                                    param.description,
                                                    param.args.map { arg ->
                                                        ArgumentModel(
                                                            id = arg.id,
                                                            name = arg.name,
                                                            type = ArgumentType.valueOf(arg.type.toString()),
                                                            nullable = arg.nullable
                                                        )
                                                    },
                                                    param.parameter,
                                                )
                                            }
                                        )
                                    )
                                }
                            }
                    }
                }
            )
        }

        item {
            AddProjectButton {
                projectName = ""
                selectedProjectType = ProjectType.LOCAL
                showCreateDialog = true
            }
        }
    }
}

@Composable
private fun CreateProjectDialog(
    projectName: String,
    onNameChange: (String) -> Unit,
    selectedType: ProjectType,
    onTypeChange: (ProjectType) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.width(400.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Создать новый проект",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Поле ввода имени
                OutlinedTextField(
                    value = projectName,
                    onValueChange = onNameChange,
                    label = { Text("Название проекта") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Выбор типа проекта
                Text(
                    text = "Тип проекта:",
                    style = MaterialTheme.typography.labelMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProjectType.entries.forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { onTypeChange(type) },
                            label = { Text(type.name) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопки действий
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onConfirm,
                        enabled = projectName.isNotBlank()
                    ) {
                        Text("Создать")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectItem(
    project: ProjectsModel,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.size(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = project.name,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun AddProjectButton(
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.size(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Добавить проект",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

private fun createRemoteProject(
    name: String,
    projectsDataStore: ProjectsDataStore,
    toParamsScreen: (String) -> Unit,
    coroutineScope: CoroutineScope
) {
    /*val project = ProjectsModel.RemoteProject(
        name = name,
        serverUrl = "https://example.com/api" // Замените на реальный URL
    )

    coroutineScope.launch {
        projectsDataStore.update(projectsDataStore.data.value + project)
        toParamsScreen(project.id) // Используем ID для удаленных проектов
    }*/
}
