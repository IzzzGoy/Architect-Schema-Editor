package ru.alexey.ndimmatrix.generator.ui.graph

import androidx.compose.ui.graphics.Color

data class GraphColors (
    val nodeColor: Color = Color.Black,
    val selectedNodeColor: Color = Color.DarkGray,
    val edgeColor: Color = Color.Blue,
    val createEdgeColor: Color = Color.Blue.copy(alpha = 0.3f),
    val deleteEdgeColor: Color = Color.Red.copy(alpha = 0.3f),
    val nodeTextColor: Color = Color.Black,
)
