package ru.alexey.ndimmatrix.generator.ui.graph

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset

// Data classes and enums
data class Edge(val from: String, val to: String) {
    override fun equals(other: Any?): Boolean {
        return other is Edge && ((from == other.from && to == other.to) || (from == other.to && to == other.from))
    }

    override fun hashCode(): Int {
        return from.hashCode() + to.hashCode()
    }
}

data class Node(val id: String, val position: Position)
data class Position(val x: Float, val y: Float)
enum class GraphEditMode {
    MOVE, DRAW, SPECTATOR, INTERACTIVE
}

interface GraphKey {
    val key: String
}

@Stable
data class GraphModel<K : GraphKey>(
    val edges: List<Edge>,
    val nodes: List<K>,
)

fun <K : GraphKey> GraphModel<K>.prettyPrint(): String {
    val nodeHeader = "┌ Nodes (${nodes.size}) ".padEnd(80, '─') + "┐"
    val edgeHeader = "┌ Edges (${edges.size}) ".padEnd(80, '─') + "┐"
    val footer = "└".padEnd(80, '─') + "┘"

    return buildString {
        appendLine("Graph Model Overview")
        appendLine(nodeHeader)

        if (nodes.isEmpty()) {
            appendLine("│  No nodes found".padEnd(80) + "│")
        } else {
            nodes.forEachIndexed { i, node ->
                val line = "│ ${i + 1}. ${node.key.padEnd(20)} [${node::class.simpleName}]"
                appendLine(line.padEnd(80) + "│")
            }
        }

        appendLine(footer)
        appendLine(edgeHeader)

        if (edges.isEmpty()) {
            appendLine("│  No edges found".padEnd(80) + "│")
        } else {
            edges.forEachIndexed { i, (from, to) ->
                val fromLabel = nodes.find { it.key == from }?.key ?: "$from (external)"
                val toLabel = nodes.find { it.key == to }?.key ?: "$to (external)"
                val line = "│ ${i + 1}. $fromLabel → $toLabel"
                appendLine(line.padEnd(80) + "│")
            }
        }

        appendLine(footer)
    }
}


@Stable
data class GraphTransform(
    val scale: Float = 1f,
    val offset: Offset = Offset.Zero
)