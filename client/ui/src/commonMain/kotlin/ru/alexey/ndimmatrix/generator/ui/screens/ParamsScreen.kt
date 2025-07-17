package ru.alexey.ndimmatrix.generator.ui.screens

import ParamEditorDrawer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Polyline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsIntents
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.ParametersHolder
import ru.alexey.ndimmatrix.generator.ui.dialogs.CreateParameterDialogWrapper
import ru.alexey.ndimmatrix.generator.ui.dialogs.InteractiveDirectedGraph
import ru.alexey.ndimmatrix.generator.ui.graph.Edge
import ru.alexey.ndimmatrix.generator.ui.graph.GraphColors
import ru.alexey.ndimmatrix.generator.ui.graph.GraphEditMode
import ru.alexey.ndimmatrix.generator.ui.graph.GraphKey
import ru.alexey.ndimmatrix.generator.ui.graph.GraphModel
import ru.alexey.ndimmatrix.generator.ui.graph.GraphTransform

@Composable
fun ParamsScreen() {
    val dialogs = koinInject<DialogsParameterHolder>()
    val coroutineScope = rememberCoroutineScope()
    val parameter = koinInject<ParametersHolder>()
    val _graphModel by parameter.flow.collectAsState()

    val graphModel by remember {
        derivedStateOf {
            GraphModel<ParamDefinition>(
                edges = _graphModel.edges.map { Edge(it.first, it.second) },
                nodes = _graphModel.nodes.map { ParamDefinition(it.name, it.description, it.args) },
            )
        }
    }

    var selectedParams by remember { mutableStateOf<ParamDefinition?>(null) }



    InteractiveGraphEditor(
        graphModel = graphModel,
        onEdgesChanged = { edges ->
            coroutineScope.launch {
                parameter.handle(
                    ParametersHolder.UpdateParameters.SetEdges(
                        edges = edges.map {
                            it.from to it.to
                        }.toSet()
                    )
                )
            }
        },
        onNodeClick = { selectedParams = it },
        initialMode = GraphEditMode.MOVE,
        onAddNode = {
            coroutineScope.launch {
                dialogs.handle(
                    DialogsIntents.AddDialog(
                        key = "CreateParameter",
                        CreateParameterDialogWrapper(
                            onCreate = {
                                coroutineScope.launch {
                                    parameter.handle(
                                        ParametersHolder.UpdateParameters.AddNode(
                                            name = it.name,
                                            description = it.description,
                                            args = it.args
                                        )
                                    )
                                }
                            },
                            onDismiss = {
                                coroutineScope.launch {
                                    dialogs.handle(
                                        DialogsIntents.RemoveDialog("CreateParameter")
                                    )
                                }
                            }
                        )
                    )
                )
            }

        }
    )

    selectedParams?.also {
        ParamEditorDrawer(
            param = it,
            onClose = { selectedParams = null },
            onParamChange = {}
        )
    }
}


data class ParamDefinition(
    val name: String,
    val description: String,
    val args: List<ArgumentModel>,
) : GraphKey {
    override val key: String
        get() = name

    fun prettyPrint(): String {
        val header = "┌─ PARAMETER DEFINITION ".padEnd(80, '─') + "┐"
        val footer = "└".padEnd(80, '─') + "┘"

        return buildString {
            appendLine(header)
            appendLine("│ Name:        ${name.padEnd(64)} │")
            appendLine("│ Description: ${description.padEnd(64)} │")
            if (args.isNotEmpty()) {
                appendLine("├─ Arguments (${args.size}) ".padEnd(80, '─') + "┤")
                args.forEach { arg ->
                    appendLine("│ ${arg.prettyPrint().replace("\n", "\n│ ")}".padEnd(80, ' ') + "│")
                }
            } else {
                appendLine("│ No arguments".padEnd(80, ' ') + "│")
            }
            appendLine(footer)
        }
    }
}

@Composable
fun GraphModeSelector(
    currentMode: GraphEditMode,
    onModeChanged: (GraphEditMode) -> Unit,
    onAddNode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isWide = LocalWindowInfo.current.containerSize.width > 500

    if (isWide) {
        HorizontalGraphModeSelector(currentMode, onModeChanged, onAddNode, modifier)
    } else {
        VerticalGraphModeSelector(currentMode, onModeChanged, onAddNode, modifier)
    }
}

@Composable
private fun ZoomControls(
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isWide = LocalWindowInfo.current.containerSize.width > 500


    val controls = remember {
        movableContentOf {
            IconButton(
                onClick = onZoomIn,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom in")
            }
            IconButton(
                onClick = onZoomOut,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom out")
            }
            HorizontalDivider(
                modifier = Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .padding(horizontal = 2.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            IconButton(
                onClick = onReset,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset view")
            }
        }
    }

    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
        )
    ) {

        if (isWide) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                controls()
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(4.dp)
            ) {
                controls()
            }
        }
    }
}

@Composable
fun<K: GraphKey> InteractiveGraphEditor(
    graphModel: GraphModel<K>,
    modifier: Modifier = Modifier,
    onEdgesChanged: (Set<Edge>) -> Unit = {},
    initialMode: GraphEditMode = GraphEditMode.MOVE,
    initialTransform: GraphTransform = GraphTransform(),
    onNodeClick: (K) -> Unit = {

    },
    onAddNode: () -> Unit,
) {
    var mode by remember { mutableStateOf(initialMode) }
    var transform by remember { mutableStateOf(initialTransform) }

    Box(modifier.fillMaxSize()) {
        // Основной граф
        InteractiveDirectedGraph(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            model = graphModel,
            onEdgesChanged = { edges ->
                onEdgesChanged(edges.toSet())
            },
            mode = mode,
            transform = transform,
            onTransformChange = { newTransform ->
                transform = newTransform
            },
            onNodeClick = onNodeClick,
            graphColors = GraphColors(
                nodeColor = MaterialTheme.colorScheme.onSurface,
                selectedNodeColor = MaterialTheme.colorScheme.primary,
                edgeColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                createEdgeColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f),
                deleteEdgeColor = MaterialTheme.colorScheme.error.copy(alpha = 0.4f),
                nodeTextColor = MaterialTheme.colorScheme.onPrimary,
            )
        )

        // Управляющие элементы поверх графа
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            // Селектор режимов
            GraphModeSelector(
                currentMode = mode,
                onModeChanged = { mode = it },
                onAddNode = onAddNode,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Кнопки масштабирования (опционально)
            ZoomControls(
                onZoomIn = {
                    transform = transform.copy(scale = (transform.scale * 1.2f).coerceAtMost(5f))
                },
                onZoomOut = {
                    transform = transform.copy(scale = (transform.scale * 0.8f).coerceAtLeast(0.5f))
                },
                onReset = { transform = GraphTransform() }
            )
        }
    }
}

@Composable
fun VerticalGraphModeSelector(
    currentMode: GraphEditMode,
    onModeChanged: (GraphEditMode) -> Unit,
    onAddNode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FilledIconButton(
            onClick = onAddNode,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add node")
        }

        HorizontalDivider(
            modifier = Modifier.width(48.dp).padding(horizontal = 8.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
        GraphEditMode.entries.forEach { mode ->
            val isSelected = mode == currentMode
            val iconTint = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)

            IconButton(
                onClick = { onModeChanged(mode) },
                modifier = Modifier.size(40.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (isSelected)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    else
                        Color.Transparent
                )
            ) {
                val icon = when (mode) {
                    GraphEditMode.MOVE -> Icons.Default.OpenWith
                    GraphEditMode.DRAW -> Icons.Default.Polyline
                    GraphEditMode.SPECTATOR -> Icons.Default.Visibility
                    GraphEditMode.INTERACTIVE -> Icons.Default.TouchApp
                }
                Icon(
                    imageVector = icon,
                    contentDescription = mode.name,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun HorizontalGraphModeSelector(
    currentMode: GraphEditMode,
    onModeChanged: (GraphEditMode) -> Unit,
    onAddNode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val modes = remember { GraphEditMode.entries }

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // Кнопка добавления узла
        FilledIconButton(
            onClick = onAddNode,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add node")
        }

        VerticalDivider(
            modifier = Modifier.height(48.dp).padding(horizontal = 8.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )

        modes.forEach { mode ->
            val isSelected = mode == currentMode
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else
                    Color.Transparent,
                animationSpec = tween(durationMillis = 200)
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor)
                    .clickable { onModeChanged(mode) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val iconTint by animateColorAsState(
                        targetValue = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else
                                    Color.Transparent,
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                else
                                    Color.Transparent,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val icon = when (mode) {
                            GraphEditMode.MOVE -> Icons.Default.OpenWith
                            GraphEditMode.DRAW -> Icons.Default.Polyline
                            GraphEditMode.SPECTATOR -> Icons.Default.Visibility
                            GraphEditMode.INTERACTIVE -> Icons.Default.TouchApp
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
                        Text(
                            text = when (mode) {
                                GraphEditMode.MOVE -> "Move"
                                GraphEditMode.DRAW -> "Draw"
                                GraphEditMode.SPECTATOR -> "View"
                                GraphEditMode.INTERACTIVE -> "Interact"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NodeCreationControls(
    onAddNode: () -> Unit,
    modifier: Modifier = Modifier,
    colors: GraphColors = GraphColors()
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Кнопка добавления узла
        FilledIconButton(
            onClick = onAddNode,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add node",
                modifier = Modifier.size(24.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(1.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )

        // Здесь можно добавить другие инструменты создания
    }
}