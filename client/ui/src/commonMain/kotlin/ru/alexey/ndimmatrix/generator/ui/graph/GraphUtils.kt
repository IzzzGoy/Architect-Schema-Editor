package ru.alexey.ndimmatrix.generator.ui.graph

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
internal fun DrawScope.drawNodes(
    nodesState: Map<String, Offset>,
    draggedNode: String?,
    dragStartNode: String?,
    graphColors: GraphColors,
    textMeasurer: TextMeasurer,
) {
    // Константы для отступов и минимального размера
    val horizontalPadding = 16f
    val verticalPadding = 8f
    val minWidth = 120f
    val minHeight = 40f
    val cornerRadius = 12f

    nodesState.forEach { (nodeId, pos) ->
        // Измеряем текст
        val textLayoutResult = textMeasurer.measure(
            text = AnnotatedString(nodeId),
            style = TextStyle(
                color = graphColors.nodeTextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            ),
            maxLines = 3,
        )

        // Рассчитываем размер ноды на основе текста
        val nodeWidth = maxOf(
            textLayoutResult.size.width + horizontalPadding * 2,
            minWidth
        )
        val nodeHeight = maxOf(
            textLayoutResult.size.height + verticalPadding * 2,
            minHeight
        )
        val nodeSize = Size(nodeWidth, nodeHeight)

        val center = pos
        val isActive = nodeId == draggedNode || nodeId == dragStartNode
        val topLeft = Offset(center.x - nodeSize.width / 2, center.y - nodeSize.height / 2)

        // Рисуем ноду
        drawRoundRect(
            color = if (isActive) graphColors.selectedNodeColor else graphColors.nodeColor,
            topLeft = topLeft,
            size = nodeSize,
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )

        // Рисуем текст по центру
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                center.x - textLayoutResult.size.width / 2,
                center.y - textLayoutResult.size.height / 2
            )
        )
    }
}


fun doLinesIntersect(p1: Offset, p2: Offset, q1: Offset, q2: Offset): Boolean {
    fun ccw(a: Offset, b: Offset, c: Offset): Boolean {
        return (c.y - a.y) * (b.x - a.x) > (b.y - a.y) * (c.x - a.x)
    }
    return (ccw(p1, q1, q2) != ccw(p2, q1, q2)) && (ccw(p1, p2, q1) != ccw(p1, p2, q2))
}

internal fun DrawScope.drawEdge(
    edge: Edge,
    nodesState: Map<String, Offset>,
    canvasWidth: Float,
    canvasHeight: Float,
    color: Color,
) {
    val fromPos = nodesState[edge.from]!!
    val toPos = nodesState[edge.to]!!

    val start = Offset(fromPos.x * canvasWidth, fromPos.y * canvasHeight)
    val end = Offset(toPos.x * canvasWidth, toPos.y * canvasHeight)

    // Линия
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = 2f
    )

    // Стрелка
    val angle = atan2(end.y - start.y, end.x - start.x)
    val arrowSize = 10f
    val arrowEnd = end - Offset(cos(angle) * 20f, sin(angle) * 20f)

    drawPath(
        path = Path().apply {
            moveTo(arrowEnd.x, arrowEnd.y)
            lineTo(
                (arrowEnd.x - arrowSize * cos(angle - PI / 6)).toFloat(),
                (arrowEnd.y - arrowSize * sin(angle - PI / 6)).toFloat()
            )
            moveTo(arrowEnd.x, arrowEnd.y)
            lineTo(
                (arrowEnd.x - arrowSize * cos(angle + PI / 6)).toFloat(),
                (arrowEnd.y - arrowSize * sin(angle + PI / 6)).toFloat()
            )
        },
        color = color,
        style = Stroke(2f)
    )
}

/*internal fun DrawScope.drawEdge(
    edge: Edge,
    nodesState: Map<String, Offset>,
    nodeSize: Size = Size(40f, 40f), // Размер прямоугольника узла
    color: Color,
) {
    val fromCenter = nodesState[edge.from]!!
    val toCenter = nodesState[edge.to]!!

    // Прямоугольник конечного узла
    val endRect = Rect(
        left = toCenter.x - nodeSize.width / 2,
        top = toCenter.y - nodeSize.height / 2,
        right = toCenter.x + nodeSize.width / 2,
        bottom = toCenter.y + nodeSize.height / 2
    )

    // Находим точку пересечения линии (центр-центр) с прямоугольником
    val endPoint = intersectLineWithRect(fromCenter, toCenter, endRect)

    // Рисуем линию от центра начального узла до границы конечного
    drawLine(
        color = color,
        start = fromCenter,
        end = endPoint,
        strokeWidth = 2f
    )

    // Рисуем стрелку
    val angle = atan2(endPoint.y - fromCenter.y, endPoint.x - fromCenter.x)
    val arrowSize = 10f
    val arrowEnd = endPoint - Offset(cos(angle) * 2f, sin(angle) * 2f) // Минимальный отступ

    drawPath(
        path = Path().apply {
            moveTo(arrowEnd.x, arrowEnd.y)
            lineTo(
                (arrowEnd.x - arrowSize * cos(angle - PI / 6)).toFloat(),
                (arrowEnd.y - arrowSize * sin(angle - PI / 6)).toFloat()
            )
            moveTo(arrowEnd.x, arrowEnd.y)
            lineTo(
                (arrowEnd.x - arrowSize * cos(angle + PI / 6)).toFloat(),
                (arrowEnd.y - arrowSize * sin(angle + PI / 6)).toFloat()
            )
        },
        color = color,
        style = Stroke(2f)
    )
}*/

internal fun DrawScope.drawEdge(
    edge: Edge,
    nodesState: Map<String, Offset>,
    nodeSize: Size = Size(200f, 60f),
    color: Color = Color.Black
) {
    val from = nodesState[edge.from] ?: return
    val to = nodesState[edge.to] ?: return

    val fromCenter = from
    val toCenter = to

    val arrowLengthOffset = 12f // уменьшение длины линии перед границей

    val fromEdge = intersectRectEdge(fromCenter, toCenter, nodeSize)
    val toEdgeFull = intersectRectEdge(toCenter, fromCenter, nodeSize)

    // Укорачиваем линию для красивого наконечника
    val lineVector = toEdgeFull - fromEdge
    val lineLength = lineVector.getDistance()
    val shortenRatio = ((lineLength - arrowLengthOffset) / lineLength).coerceAtLeast(0f)

    val toEdge = fromEdge + (lineVector * shortenRatio)

    // Основная линия
    drawLine(
        color = color,
        start = fromEdge,
        end = toEdge,
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )

    // Стрелка
    val angle = atan2(toEdgeFull.y - fromEdge.y, toEdgeFull.x - fromEdge.x)
    val arrowSize = 20f
    val arrowAngle = PI / 8

    val tip = toEdgeFull
    val left = Offset(
        (tip.x - arrowSize * cos(angle - arrowAngle)).toFloat(),
        (tip.y - arrowSize * sin(angle - arrowAngle)).toFloat()
    )
    val right = Offset(
        (tip.x - arrowSize * cos(angle + arrowAngle)).toFloat(),
        (tip.y - arrowSize * sin(angle + arrowAngle)).toFloat()
    )

    drawPath(
        Path().apply {
            moveTo(tip.x, tip.y)
            lineTo(left.x, left.y)
            lineTo(right.x, right.y)
            close()
        },
        color = color
    )
}


fun intersectRectEdge(
    center: Offset,
    target: Offset,
    size: Size
): Offset {
    val halfWidth = size.width / 2
    val halfHeight = size.height / 2

    val dx = target.x - center.x
    val dy = target.y - center.y

    val scaleX = if (dx != 0f) halfWidth / abs(dx) else Float.POSITIVE_INFINITY
    val scaleY = if (dy != 0f) halfHeight / abs(dy) else Float.POSITIVE_INFINITY
    val scale = min(scaleX, scaleY)

    return Offset(
        center.x + dx * scale,
        center.y + dy * scale
    )
}


private fun intersectLineWithRect(center: Offset, target: Offset, rect: Rect): Offset {
    // Если цель внутри прямоугольника - возвращаем центр (защита от ошибок)
    if (rect.contains(target)) return target

    // Направляющий вектор линии
    val direction = target - center
    val dirNorm = direction / direction.getDistance()

    // Проверяем пересечение с каждой стороной прямоугольника
    val sides = listOf(
        // Левая грань
        Rect(rect.left, rect.top, rect.left, rect.bottom),
        // Правая грань
        Rect(rect.right, rect.top, rect.right, rect.bottom),
        // Верхняя грань
        Rect(rect.left, rect.top, rect.right, rect.top),
        // Нижняя грань
        Rect(rect.left, rect.bottom, rect.right, rect.bottom)
    )

    // Находим все точки пересечения
    val intersections = sides.mapNotNull { side ->
        lineIntersection(
            center,
            direction,
            side.left, side.top,
            side.right, side.bottom
        )
    }.filter { it != center }

    // Выбираем ближайшую к центру точку пересечения
    return intersections.minByOrNull { (it - center).getDistance() } ?: target
}


private fun lineIntersection(
    origin: Offset,
    direction: Offset,
    x1: Float, y1: Float,
    x2: Float, y2: Float
): Offset? {
    // Реализация алгоритма пересечения линии и отрезка
    val segmentVec = Offset(x2 - x1, y2 - y1)
    val segmentPerp = Offset(-segmentVec.y, segmentVec.x)

    val dot = direction.x * segmentPerp.x + direction.y * segmentPerp.y
    if (abs(dot) < 1e-6) return null // Параллельны

    val t = ((x1 - origin.x) * segmentPerp.x + (y1 - origin.y) * segmentPerp.y) / dot
    val u = ((origin.x - x1) * direction.y - (origin.y - y1) * direction.x) /
            (segmentVec.x * direction.y - segmentVec.y * direction.x)

    if (u in 0f..1f && t >= 0) {
        return origin + direction * t
    }
    return null
}

internal fun findNodeAtPosition(
    nodesState: Map<String, Offset>, // нормализованные координаты
    position: Offset,
    width: Float = 200f, // половина ширины/высоты прямоугольника
    height: Float = 60f, // половина ширины/высоты прямоугольника
): String? {
    return nodesState.entries.firstOrNull { (_, normPos) ->
        val centerX = normPos.x
        val centerY = normPos.y

        val left = centerX - width / 2
        val right = centerX + width / 2
        val top = centerY - height / 2
        val bottom = centerY + height / 2

        position.x in left..right && position.y in top..bottom
    }?.key
}

fun calculateNodePositions(allNodes: List<GraphKey>, edges: List<Edge>): List<Node> {
    if (allNodes.isEmpty()) return emptyList()

    val allNodeIds = allNodes.map(GraphKey::key).toSet()

    // Граф и входящие рёбра
    val nodesWithIncoming = edges.map { it.to }.toSet()
    val startNodes = allNodeIds - nodesWithIncoming

    val roots = if (startNodes.isEmpty()) {
        listOf(edges.firstOrNull()?.from ?: allNodeIds.first())
    } else {
        startNodes.toList()
    }

    val graph = mutableMapOf<String, MutableList<String>>()
    edges.forEach { edge ->
        graph.getOrPut(edge.from) { mutableListOf() }.add(edge.to)
    }

    // BFS — вычисляем уровни
    val levels = mutableMapOf<String, Int>()
    val queue = ArrayDeque<String>()
    roots.forEach { root ->
        levels[root] = 0
        queue.add(root)
    }

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        graph[current]?.forEach { neighbor ->
            if (neighbor !in levels) {
                levels[neighbor] = levels[current]!! + 1
                queue.add(neighbor)
            }
        }
    }

    // Добавляем одиночные ноды на "нижний" уровень
    val defaultLevel = (levels.values.maxOrNull() ?: 0) + 1
    val allLevels = allNodeIds.associateWith { nodeId -> levels[nodeId] ?: defaultLevel }

    val nodesByLevel = allLevels.entries.groupBy({ it.value }, { it.key })
    val maxLevel = nodesByLevel.keys.maxOrNull() ?: 0

    // Параметры абсолютного позиционирования
    val xStep = 300f
    val yStep = 200f

    return allNodeIds.map { nodeId ->
        val level = allLevels[nodeId] ?: 0
        val nodesInLevel = nodesByLevel[level] ?: listOf()
        val indexInLevel = nodesInLevel.indexOf(nodeId)

        val x = indexInLevel * xStep + xStep
        val y = level * yStep + yStep

        Node(nodeId, Position(x, y))
    }
}

