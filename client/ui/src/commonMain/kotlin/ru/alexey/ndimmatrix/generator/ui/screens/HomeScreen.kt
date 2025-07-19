package ru.alexey.ndimmatrix.generator.ui.screens

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.ProjectsListHolder

@Composable
fun HomeScreen() {
    val projects = koinInject<ProjectsListHolder>()
    val state by projects.flow.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.FixedSize(100.dp)
    ) {
        items(state.toList()) { project ->

        }
    }
}
