package ru.alexey.ndimmatrix.generator.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ru.alexey.ndimmatrix.generator.presentation.api.chains.CreateParamEventChain
import ru.alexey.ndimmatrix.generator.presentation.api.handlers.SaveArgumentEventHandler
import ru.alexey.ndimmatrix.generator.presentation.api.models.ParameterModel
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.CreateParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogWrapper
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsIntents
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsParameterHolder

@OptIn(ExperimentalMaterial3Api::class)
data class CreateParameterDialogWrapper(
    val onCreate: (ParameterModel) -> Unit,
    val onDismiss: () -> Unit,
) : DialogWrapper {

    companion object {
        const val KEY = "CreateParameterDialogWrapper"
    }

    @Composable
    override fun Content() {

        val parameter = koinInject<CreateParameterHolder>()
        val dialogs = koinInject<DialogsParameterHolder>()

        val state by parameter.flow.collectAsState()
        val coroutineScope = rememberCoroutineScope()

        val createParameterEventChain = koinInject<CreateParamEventChain>()

        Dialog(
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = Modifier
                    .widthIn(min = 600.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = MaterialTheme.shapes.medium
                    )
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Create Parameter",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                        )
                    }
                )

                TextField(
                    value = state.name,
                    onValueChange = {
                        coroutineScope.launch {
                            parameter.handle(
                                CreateParameterHolder.CreateParameterIntents.SetParameterName(it)
                            )
                        }
                    },
                    prefix = {
                        Text(
                            text = "Parameter name:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 4,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        disabledIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        errorContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    shape = RoundedCornerShape(50),
                )
                TextField(
                    value = state.description,
                    onValueChange = {
                        coroutineScope.launch {
                            parameter.handle(
                                CreateParameterHolder.CreateParameterIntents.SetParameterDescription(it)
                            )
                        }
                    },
                    prefix = {
                        Text(
                            text = "Parameter description:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 4,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        disabledIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        errorContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    shape = RoundedCornerShape(50),
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(state.args) { arg ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = arg.name,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        )
                    }
                    item {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    dialogs.handle(
                                        e = DialogsIntents.AddDialog(
                                            key = "ArgumentsCreations",
                                            dialog = CreateArgumentDialogWrapper(
                                                onSubmit = {
                                                    coroutineScope.launch {
                                                        parameter.handle(
                                                            CreateParameterHolder.CreateParameterIntents.AddArgument(
                                                                it
                                                            )
                                                        )
                                                    }
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
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }


                ElevatedButton(
                    onClick = {
                        coroutineScope.launch {
                            createParameterEventChain.general(
                                CreateParamEventChain.CreateParamEvent.CreateParameter(
                                    state.name,
                                    state.description,
                                    state.args,
                                )
                            )
                        }
                        onDismiss()
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                ) {
                    Text(
                        text = "Create",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
