package ru.alexey.ndimmatrix.generator.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ru.alexey.ndimmatrix.generator.data.api.local.ProjectLocalSaver
import ru.alexey.ndimmatrix.generator.data.api.models.ArgumentDataModel
import ru.alexey.ndimmatrix.generator.data.api.models.ArgumentDataType
import ru.alexey.ndimmatrix.generator.data.api.models.EventDataModel
import ru.alexey.ndimmatrix.generator.data.api.models.EventsDataModel
import ru.alexey.ndimmatrix.generator.data.api.models.ParameterDataModel
import ru.alexey.ndimmatrix.generator.data.api.models.ParametersDataModel
import ru.alexey.ndimmatrix.generator.data.api.models.ProjectDataModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentType
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsIntents
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.EventsHolder
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.ParametersHolder
import ru.alexey.ndimmatrix.generator.ui.dialogs.CreateEventDialogWrapper
import ru.alexey.ndimmatrix.generator.ui.graph.Edge
import ru.alexey.ndimmatrix.generator.ui.graph.GraphEditMode
import ru.alexey.ndimmatrix.generator.ui.graph.GraphKey
import ru.alexey.ndimmatrix.generator.ui.graph.GraphModel

@Composable
fun EventsScreen(
    project: String,
    toParamsScreen: () -> Unit,
) {

    val dialogs = koinInject<DialogsParameterHolder>()
    val coroutineScope = rememberCoroutineScope()
    val parameter = koinInject<EventsHolder>()
    val _graphModel by parameter.flow.collectAsState()
    val saver = koinInject<ProjectLocalSaver>()

    val params = koinInject<ParametersHolder>()


    val graphModel by remember {
        derivedStateOf {
            GraphModel(
                edges = _graphModel.edges.map { Edge(it.first, it.second) },
                nodes = _graphModel.nodes.map {
                    EventDefinition(
                        it.parameter,
                        it.name,
                        it.description,
                        it.args
                    )
                },
            )
        }
    }

    InteractiveGraphEditor(
        graphModel = graphModel,
        initialMode = GraphEditMode.MOVE,
        onEdgesChanged = { edges ->
            coroutineScope.launch {
                parameter.handle(
                    EventsHolder.EventHolderIntents.SetEdges(
                        edges = edges.map {
                            it.from to it.to
                        }.toSet()
                    )
                )
            }
        },
        save = {
            coroutineScope.launch {
                saver.save(
                    path = project,
                    data = ProjectDataModel(
                        parametersWorkspaces = listOf(
                            ParametersDataModel(
                                edges = params.value.edges,
                                nodes = params.value.nodes.map {
                                    ParameterDataModel(
                                        name = it.name,
                                        description = it.description,
                                        args = it.args.map {
                                            ArgumentDataModel(
                                                id = it.id,
                                                name = it.name,
                                                type = when (it.type) {
                                                    ArgumentType.INT -> ArgumentDataType.INT
                                                    ArgumentType.FLOAT -> ArgumentDataType.FLOAT
                                                    ArgumentType.STRING -> ArgumentDataType.STRING
                                                    ArgumentType.BOOLEAN -> ArgumentDataType.BOOLEAN
                                                    ArgumentType.LONG -> ArgumentDataType.LONG
                                                    ArgumentType.DOUBLE -> ArgumentDataType.DOUBLE
                                                    ArgumentType.CHAR -> ArgumentDataType.CHAR
                                                    ArgumentType.BYTE -> ArgumentDataType.BYTE
                                                    ArgumentType.SHORT -> ArgumentDataType.SHORT
                                                },
                                                nullable = it.nullable
                                            )
                                        }
                                    )
                                }
                            ),
                        ),
                        eventsWorkspaces = listOf(
                            EventsDataModel(
                                edges = _graphModel.edges,
                                nodes = _graphModel.nodes.map {
                                    EventDataModel(
                                        parameter = it.parameter,
                                        name = it.name,
                                        description = it.description,
                                        args = it.args.map {
                                            ArgumentDataModel(
                                                id = it.id,
                                                name = it.name,
                                                type = when (it.type) {
                                                    ArgumentType.INT -> ArgumentDataType.INT
                                                    ArgumentType.FLOAT -> ArgumentDataType.FLOAT
                                                    ArgumentType.STRING -> ArgumentDataType.STRING
                                                    ArgumentType.BOOLEAN -> ArgumentDataType.BOOLEAN
                                                    ArgumentType.LONG -> ArgumentDataType.LONG
                                                    ArgumentType.DOUBLE -> ArgumentDataType.DOUBLE
                                                    ArgumentType.CHAR -> ArgumentDataType.CHAR
                                                    ArgumentType.BYTE -> ArgumentDataType.BYTE
                                                    ArgumentType.SHORT -> ArgumentDataType.SHORT
                                                },
                                                nullable = it.nullable
                                            )
                                        }
                                    )
                                },
                            )
                        )
                    )
                )
            }
        },
        onAddNode = {
            coroutineScope.launch {
                dialogs.handle(
                    DialogsIntents.AddDialog(
                        key = "CreateEventDialogWrapper",
                        CreateEventDialogWrapper(
                            onSubmit = {
                                coroutineScope.launch {
                                    parameter.handle(
                                        EventsHolder.EventHolderIntents.AddNode(
                                            name = it.name,
                                            parameter = it.parameter,
                                            args = it.args,
                                            description = it.description,
                                        )
                                    )

                                }
                            },
                            onDismiss = {
                                coroutineScope.launch {
                                    dialogs.handle(
                                        DialogsIntents.RemoveDialog("CreateEventDialogWrapper")
                                    )
                                }
                            }
                        )
                    )
                )
            }
        },
        onNodeClick = {},
        switchMode = toParamsScreen,
    )
}


data class EventDefinition(
    val parameter: String?,
    val name: String,
    val description: String,
    val args: List<ArgumentModel>,
) : GraphKey {
    override val key: String
        get() = name
}