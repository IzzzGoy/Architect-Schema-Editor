package ru.alexey.ndimmatrix.generator.ui.screens

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
}


