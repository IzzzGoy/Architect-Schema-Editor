package ru.alexey.ndimmatrix.generator.ui.dialogs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ru.alexey.ndimmatrix.generator.presentation.api.handlers.SaveArgumentEventHandler
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogWrapper
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsIntents
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsParameterHolder
import ru.alexey.ndimmatrix.generator.ui.chains.SaveArgumentChain
import ru.alexey.ndimmatrix.generator.ui.graph.Edge
import ru.alexey.ndimmatrix.generator.ui.graph.GraphColors
import ru.alexey.ndimmatrix.generator.ui.graph.GraphEditMode
import ru.alexey.ndimmatrix.generator.ui.graph.GraphKey
import ru.alexey.ndimmatrix.generator.ui.graph.GraphModel
import ru.alexey.ndimmatrix.generator.ui.graph.GraphTransform
import ru.alexey.ndimmatrix.generator.ui.graph.calculateNodePositions
import ru.alexey.ndimmatrix.generator.ui.graph.doLinesIntersect
import ru.alexey.ndimmatrix.generator.ui.graph.drawEdge
import ru.alexey.ndimmatrix.generator.ui.graph.drawNodes
import ru.alexey.ndimmatrix.generator.ui.graph.findNodeAtPosition
import ru.alexey.ndimmatrix.generator.uikit.comtainers.intents.IntentsArgumentsRow
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.uuid.ExperimentalUuidApi

data class ConfigIntentInfoDialogWrapper(
    val onDismiss: () -> Unit
) : DialogWrapper {
    @Composable
    override fun Content() {
        ConfigIntentInfoDialog(onDismiss)
    }

}

@OptIn(ExperimentalUuidApi::class)
@Composable
internal fun ConfigIntentInfoDialog(
    onDismiss: () -> Unit
) {
    var args by remember {
        mutableStateOf(emptyList<ArgumentModel>())
    }

    val dialogsParameterHolder = koinInject<DialogsParameterHolder>()
    val coroutineScope = rememberCoroutineScope()
    val saveArgumentChain = koinInject<SaveArgumentChain>()
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .width(500.dp)
                .height(700.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(args) {
                    IntentsArgumentsRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        argId = it.id,
                        argName = it.name,
                        argType = it.type.toString(),
                        isNullable = it.nullable
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                ElevatedButton(
                    onClick = {
                        coroutineScope.launch {
                            dialogsParameterHolder.handle(
                                DialogsIntents.AddDialog(
                                    "ArgumentsCreations",
                                    CreateArgumentDialogWrapper(
                                        onSubmit = {
                                            saveArgumentChain.general(
                                                SaveArgumentEventHandler.SaveArgumentEvent(it)
                                            )
                                            args += it
                                        },
                                        onDismiss = {
                                            coroutineScope.launch {
                                                dialogsParameterHolder.handle(
                                                    e = DialogsIntents.RemoveDialog(
                                                        key = "ArgumentsCreations"
                                                    )
                                                )
                                            }
                                        }
                                    )
                                )
                            )
                        }
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                ) {
                    Text(
                        text = "Submit",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                ElevatedButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                ) {
                    Text(
                        text = "Dismiss",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}


/*@Composable
fun<K: GraphKey> InteractiveDirectedGraph(
    modifier: Modifier = Modifier,
    mode: GraphEditMode = GraphEditMode.MOVE,
    onEdgesChanged: (List<Edge>) -> Unit = {},
    onNodeClick: (K) -> Unit = {},
    graphColors: GraphColors = GraphColors(),
    model: GraphModel<K>,
    transform: GraphTransform,
    onTransformChange: ((GraphTransform) -> Unit)? = null,
) {

    val textMeasurer = rememberTextMeasurer()
    // Состояние рёбер
    var edges by remember { mutableStateOf(model.edges) }


    // Рассчитываем начальные позиции узлов
    val initialNodes = remember(model.nodes) { calculateNodePositions(model.nodes, edges) }

    // Состояние позиций узлов
    val nodesState = remember(initialNodes) { mutableStateMapOf<String, Offset>() }

    // Инициализация позиций
    LaunchedEffect(initialNodes) {
        if (nodesState.isEmpty()) {
            initialNodes.forEach { node ->
                nodesState[node.id] = Offset(node.position.x, node.position.y)
            }
        }
    }

    // Состояния для перетаскивания
    var draggedNode by remember { mutableStateOf<String?>(null) }
    var dragStartNode by remember { mutableStateOf<String?>(null) }
    var dragStartPosition by remember { mutableStateOf(Offset.Zero) }
    var dragCurrentPosition by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }

    // Размеры Canvas
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    val localTransform = remember { mutableStateOf(transform) }
    val currentTransform = if (onTransformChange == null) localTransform.value else transform

    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        LaunchedEffect(maxWidth, maxHeight) {
            with(density) {
                canvasSize = IntSize(maxWidth.roundToPx(), maxHeight.roundToPx())
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
            .pointerInput(mode, edges) {
                when (mode) {
                    GraphEditMode.SPECTATOR -> {
                        detectTransformGestures(
                            onGesture = { centroid, pan, zoom, _ ->
                                if (onTransformChange != null) {
                                    onTransformChange(transform.copy(zoom, pan))
                                }
                            }
                        )
                    }
                    GraphEditMode.INTERACTIVE -> {
                        detectTapGestures(
                            onTap = { offset ->
                                val transformedOffset = (offset - currentTransform.offset) / currentTransform.scale
                                val node = findNodeAtPosition(nodesState, canvasSize, transformedOffset)
                                if (node != null) {
                                    model.nodes.firstOrNull { it.key == node }?.let(onNodeClick)
                                }
                            }
                        )
                    }
                    else -> {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val transformedOffset = (offset - currentTransform.offset) / currentTransform.scale
                                val node = findNodeAtPosition(nodesState, canvasSize, transformedOffset)
                                when (mode) {
                                    GraphEditMode.MOVE -> {
                                        draggedNode = node
                                    }

                                    GraphEditMode.DRAW -> {
                                        dragStartPosition = transformedOffset
                                        dragCurrentPosition = transformedOffset
                                        dragStartNode = findNodeAtPosition(nodesState, canvasSize, transformedOffset)
                                        isDragging = true
                                    }

                                    else -> {}
                                }
                            },
                            onDrag = { change, dragAmount ->
                                val transformedDrag = dragAmount / currentTransform.scale
                                when (mode) {
                                    GraphEditMode.MOVE -> {
                                        draggedNode?.let { nodeId ->
                                            nodesState[nodeId] = nodesState[nodeId]!!.let { pos ->
                                                Offset(
                                                    (pos.x + transformedDrag.x / canvasSize.width)
                                                        .coerceIn(0.05f, 0.95f),
                                                    (pos.y + transformedDrag.y / canvasSize.height)
                                                        .coerceIn(0.05f, 0.95f)
                                                )
                                            }
                                        }
                                    }

                                    GraphEditMode.DRAW -> {
                                        dragCurrentPosition += transformedDrag
                                    }

                                    else -> {}

                                }
                                change.consume()
                            },
                            onDragEnd = {
                                when (mode) {
                                    GraphEditMode.MOVE -> {
                                        draggedNode = null
                                    }

                                    GraphEditMode.DRAW -> {
                                        isDragging = false

                                        val endNode = findNodeAtPosition(nodesState, canvasSize, dragCurrentPosition)

                                        if (dragStartNode != null && endNode != null && dragStartNode != endNode) {
                                            // Рисуем стрелку между вершинами
                                            val newEdge = Edge(dragStartNode!!, endNode)
                                            if (!edges.contains(newEdge)) {
                                                edges = edges + newEdge
                                                onEdgesChanged(edges)
                                            }
                                        } else if (dragStartNode == null) {
                                            // Линия пересечения — проверка на пересечение с рёбрами
                                            val intersecting = edges.filter { edge ->
                                                val from = nodesState[edge.from]!!
                                                val to = nodesState[edge.to]!!
                                                val start = Offset(from.x * canvasSize.width, from.y * canvasSize.height)
                                                val end = Offset(to.x * canvasSize.width, to.y * canvasSize.height)

                                                doLinesIntersect(
                                                    dragStartPosition,
                                                    dragCurrentPosition,
                                                    start,
                                                    end
                                                )
                                            }

                                            if (intersecting.isNotEmpty()) {
                                                edges = edges - intersecting.toSet()
                                                onEdgesChanged(edges)
                                            }
                                        }

                                        dragStartNode = null
                                    }

                                    else -> {}

                                }
                            }
                        )
                    }
                }
            }
        ) {

            withTransform({
                scale(currentTransform.scale)
                translate(left = currentTransform.offset.x, top = currentTransform.offset.y)
            }) {
                val canvasWidth = size.width / currentTransform.scale
                val canvasHeight = size.height / currentTransform.scale



                // Рисуем рёбра
                edges.forEach { edge ->
                    drawEdge(edge, nodesState, Size(200f, 60f), graphColors.edgeColor)
                }

                // Рисуем временное ребро при перетаскивании в режиме DRAW
                if (mode == GraphEditMode.DRAW && isDragging && dragStartNode != null) {
                    val startPos = nodesState[dragStartNode]?.let {
                        Offset(it.x * canvasWidth, it.y * canvasHeight)
                    } ?: dragStartPosition

                    drawLine(
                        color = graphColors.createEdgeColor,
                        start = startPos,
                        end = dragCurrentPosition,
                        strokeWidth = 3f
                    )
                } else if (isDragging) {
                    val start = dragStartNode?.let {
                        nodesState[it]?.let { pos ->
                            Offset(pos.x * canvasWidth, pos.y * canvasHeight)
                        }
                    } ?: dragStartPosition

                    drawLine(
                        color = graphColors.deleteEdgeColor,
                        start = start,
                        end = dragCurrentPosition,
                        strokeWidth = 4f
                    )
                }

                // Рисуем узлы
                drawNodes(
                    nodesState,
                    canvasWidth,
                    canvasHeight,
                    draggedNode,
                    dragStartNode,
                    graphColors,
                    textMeasurer
                )
            }
        }
    }
}*/


@Composable
fun<K: GraphKey> InteractiveDirectedGraph(
    modifier: Modifier = Modifier,
    mode: GraphEditMode = GraphEditMode.MOVE,
    onEdgesChanged: (List<Edge>) -> Unit = {},
    onNodeClick: (K) -> Unit = {},
    graphColors: GraphColors = GraphColors(),
    model: GraphModel<K>,
    transform: GraphTransform,
    onTransformChange: ((GraphTransform) -> Unit)? = null,
) {

    val textMeasurer = rememberTextMeasurer()
    // Состояние рёбер
    var edges by remember { mutableStateOf(model.edges) }

    var localTransform by remember { mutableStateOf(transform) }

    LaunchedEffect(localTransform) {
        onTransformChange?.invoke(localTransform)
    }

    LaunchedEffect(transform.scale) {
        localTransform = localTransform.copy(scale = transform.scale)
    }
    // Рассчитываем начальные позиции узлов
    val initialNodes = remember(model.nodes) { calculateNodePositions(model.nodes, edges) }

    // Состояние позиций узлов
    val nodesState = remember(initialNodes) { mutableStateMapOf<String, Offset>() }

    // Инициализация позиций
    LaunchedEffect(initialNodes) {
        if (nodesState.isEmpty()) {
            initialNodes.forEach { node ->
                nodesState[node.id] = Offset(node.position.x, node.position.y) // логические координаты (0..1)
            }
        }
    }


    // Состояния для перетаскивания
    var draggedNode by remember { mutableStateOf<String?>(null) }
    var dragStartNode by remember { mutableStateOf<String?>(null) }
    var dragStartPosition by remember { mutableStateOf(Offset.Zero) }
    var random by remember { mutableStateOf(Offset.Zero) }
    var dragCurrentPosition by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }


    BoxWithConstraints(modifier = modifier) {

        Canvas(
            modifier = Modifier
                .fillMaxSize()
            .pointerInput(mode, edges, transform.scale, nodesState) {
                when (mode) {
                    GraphEditMode.SPECTATOR -> {
                        detectTransformGestures(
                            onGesture = { _, pan, zoom, _ ->
                                println(pan)
                                val newZoom = localTransform.scale
                                val newOffset = localTransform.offset + pan
                                localTransform = GraphTransform(newZoom, newOffset)
                            }
                        )
                    }

                    GraphEditMode.INTERACTIVE -> {
                        detectTapGestures(
                            onTap = { offset ->
                                val transformedOffset = (offset - transform.offset) / transform.scale
                                val node = findNodeAtPosition(nodesState, transformedOffset)
                                if (node != null) {
                                    model.nodes.firstOrNull { it.key == node }?.let(onNodeClick)
                                }
                            }
                        )
                    }

                    else -> {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val transformedOffset = (offset - transform.offset) / transform.scale
                                random = offset
                                val node = findNodeAtPosition(nodesState, transformedOffset)
                                when (mode) {
                                    GraphEditMode.MOVE -> {
                                        draggedNode = node
                                    }

                                    GraphEditMode.DRAW -> {
                                        dragStartPosition = transformedOffset
                                        dragCurrentPosition = transformedOffset
                                        dragStartNode = node
                                        isDragging = true
                                    }

                                    else -> {}
                                }
                            },

                            onDrag = { change, dragAmount ->
                                val transformedDrag = dragAmount / transform.scale
                                when (mode) {
                                    GraphEditMode.MOVE -> {
                                        draggedNode?.let { nodeId ->
                                            nodesState[nodeId] = nodesState[nodeId]!! + transformedDrag
                                        }
                                    }

                                    GraphEditMode.DRAW -> {
                                        dragCurrentPosition += transformedDrag
                                    }

                                    else -> {}
                                }
                                change.consume()
                            },

                            onDragEnd = {
                                when (mode) {
                                    GraphEditMode.MOVE -> {
                                        draggedNode = null
                                    }

                                    GraphEditMode.DRAW -> {
                                        isDragging = false

                                        val endNode = findNodeAtPosition(nodesState, dragCurrentPosition)

                                        if (dragStartNode != null && endNode != null && dragStartNode != endNode) {
                                            val newEdge = Edge(dragStartNode!!, endNode)
                                            if (!edges.contains(newEdge)) {
                                                edges = edges + newEdge
                                                onEdgesChanged(edges)
                                            }
                                        } else if (dragStartNode == null) {
                                            val intersecting = edges.filter { edge ->
                                                val from = nodesState[edge.from]!!
                                                val to = nodesState[edge.to]!!

                                                doLinesIntersect(
                                                    dragStartPosition,
                                                    dragCurrentPosition,
                                                    from,
                                                    to
                                                )
                                            }

                                            if (intersecting.isNotEmpty()) {
                                                edges = edges - intersecting.toSet()
                                                onEdgesChanged(edges)
                                            }
                                        }

                                        dragStartNode = null
                                    }

                                    else -> {}
                                }
                            }
                        )
                    }
                }
            }
        ) {

            withTransform({
                scale(transform.scale, Offset.Zero)
                translate(left = localTransform.offset.x, top = localTransform.offset.y)
            }) {
                val canvasWidth = size.width * transform.scale
                val canvasHeight = size.height * transform.scale



                // Рисуем рёбра
                edges.forEach { edge ->
                    drawEdge(edge, nodesState, Size(200f, 60f), graphColors.edgeColor)
                }

                // Рисуем временное ребро при перетаскивании в режиме DRAW
                if (mode == GraphEditMode.DRAW && isDragging && dragStartNode != null) {
                    val startPos = nodesState[dragStartNode] ?: dragStartPosition

                    // Внутри withTransform { scale + translate }
                    drawLine(
                        color = graphColors.createEdgeColor,
                        start = nodesState[dragStartNode] ?: dragStartPosition,
                        end = dragCurrentPosition,
                        strokeWidth = 3f
                    )


                } else if (isDragging) {
                    val start = dragStartNode?.let {
                        nodesState[it]?.let { pos ->
                            Offset(pos.x * canvasWidth, pos.y * canvasHeight)
                        }
                    } ?: dragStartPosition

                    // Внутри withTransform { scale + translate }
                    drawLine(
                        color = graphColors.createEdgeColor,
                        start = start ?: nodesState[dragStartNode] ?: dragStartPosition,
                        end = dragCurrentPosition,
                        strokeWidth = 3f
                    )


                }

                // Рисуем узлы
                drawNodes(
                    nodesState,
                    draggedNode,
                    dragStartNode,
                    graphColors,
                    textMeasurer,
                )
            }
        }
    }
}

