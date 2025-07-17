package ru.alexey.ndimmatrix

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import ru.alexey.ndimmatrix.generator.presentation.api.handlers.SaveArgumentEventHandler
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.ParameterModel
import ru.alexey.ndimmatrix.generator.ui.chains.SaveArgumentChain
import ru.alexey.ndimmatrix.generator.ui.dialogs.CreateArgumentDialogWrapper
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsIntents
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.ParametersHolder
import ru.alexey.ndimmatrix.generator.ui.Navigator
import ru.alexey.ndimmatrix.generator.ui.dialogs.ConfigIntentInfoDialogWrapper
import ru.alexey.ndimmatrix.generator.ui.dialogs.CreateParameterDialogWrapper
import ru.alexey.ndimmatrix.generator.ui.dialogs.InteractiveDirectedGraph
import ru.alexey.ndimmatrix.generator.ui.graph.Edge
import ru.alexey.ndimmatrix.generator.ui.graph.GraphEditMode
import ru.alexey.ndimmatrix.generator.ui.graph.GraphKey
import ru.alexey.ndimmatrix.generator.ui.graph.GraphModel
import ru.alexey.ndimmatrix.generator.ui.graph.GraphTransform
import ru.alexey.ndimmatrix.generator.ui.graph.prettyPrint
import ru.alexey.ndimmatrix.generator.uikit.theme.ConfigTheme


@Preview
@Composable
internal fun App() = ConfigTheme {
    val dialogs = koinInject<DialogsParameterHolder>()
    val coroutineScope = rememberCoroutineScope()

    val saveArgumentChain: SaveArgumentChain = koinInject()
    val parameter = koinInject<ParametersHolder>()
    LaunchedEffect(Unit) {
        /*dialogs.handle(
            e = DialogsIntents.AddDialog(
                key = "ArgumentsCreations",
                dialog = CreateArgumentDialogWrapper(
                    onSubmit = {
                        saveArgumentChain.general(
                            SaveArgumentEventHandler.SaveArgumentEvent(it)
                        )
                    },
                    onDismiss = {
                        coroutineScope.launch {
                            dialogs.handle(
                                e = DialogsIntents.RemoveDialog(
                                    key = "ArgumentsCreations"
                                )
                            )
                        }
                    }
                )
            )
        )*/

        /*dialogs.handle(
            e = DialogsIntents.AddDialog(
                "EditIntent",
                ConfigIntentInfoDialogWrapper(
                    onDismiss = {
                        coroutineScope.launch {
                            dialogs.handle(
                                e = DialogsIntents.RemoveDialog("EditIntent")
                            )
                        }
                    }
                )
            )
        )*/
    }



    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues: PaddingValues ->
        Navigator(Modifier.padding(paddingValues))
    }



/*
    LaunchedEffect(graphModel) {
        println(graphModel.prettyPrint())
        val (params, projections) = graphModel.nodes.partition { it.key !in graphModel.edges.map { it.to } }
        val paramsHeader = "┌ Params (${params.size}) ".padEnd(80, '─') + "┐"
        val projectionsHeader = "┌ Projections (${projections.size}) ".padEnd(80, '─') + "┐"
        val footer = "└".padEnd(80, '─') + "┘"

        println(paramsHeader)
        params.forEach {
            println("| ${it.key}".padEnd(80, ' ') + "|" )
        }
        println(footer)

        println(projectionsHeader)
        projections.forEach {
            println("| ${it.key}".padEnd(80, ' ') + "|" )
        }
        println(footer)
    }*/




    val state by dialogs.flow.collectAsState()
    state.values.forEach {
        it.Content()
    }
}

