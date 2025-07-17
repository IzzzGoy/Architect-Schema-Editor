package ru.alexey.ndimmatrix.generator.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.CreationArgumentIntents
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.CreationArgumentParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogWrapper
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsIntents
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsParameterHolder


@Immutable
data class CreateArgumentDialogWrapper(
    val onSubmit: (ArgumentModel) -> Unit,
    val onDismiss: () -> Unit,
): DialogWrapper {
    @Composable
    override fun Content() {
        CreateArgumentDialog(this)
    }
    @Composable
    private fun CreateArgumentDialog(
        metadata: CreateArgumentDialogWrapper
    ) {
        val dialog = koinInject<DialogsParameterHolder>()
        val parameter = koinInject<CreationArgumentParameterHolder>()
        val state by parameter.flow.collectAsState()
        val coroutineScope = rememberCoroutineScope()
        Dialog(
            onDismissRequest = metadata.onDismiss
        ) {
            Column(
                modifier = Modifier
                    .width(400.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = MaterialTheme.shapes.medium
                    )
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Create Argument",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Argument name:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    BasicTextField(
                        state.name,
                        {
                            coroutineScope.launch {
                                parameter.handle(
                                    CreationArgumentIntents.SetArgumentName(
                                        it
                                    )
                                )
                            }
                        },
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Is nullable:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Checkbox(
                        checked = state.nullable,
                        onCheckedChange = {
                            coroutineScope.launch {
                                parameter.handle(
                                    CreationArgumentIntents.SetArgumentNullable(
                                        it
                                    )
                                )
                            }
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Type:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    ElevatedButton(
                        onClick = {
                            coroutineScope.launch {
                                dialog.handle(
                                    DialogsIntents.AddDialog(
                                        key = "SelectArgumentType",
                                        dialog = SelectArgumentTypeWrapper(
                                            onDismiss = {
                                                coroutineScope.launch {
                                                    dialog.handle(
                                                        DialogsIntents.RemoveDialog(
                                                            key = "SelectArgumentType"
                                                        )
                                                    )
                                                }
                                            },
                                            onSelect = {
                                                coroutineScope.launch {
                                                    parameter.handle(
                                                        CreationArgumentIntents.SetArgumentType(
                                                            it
                                                        )
                                                    )
                                                    dialog.handle(
                                                        DialogsIntents.RemoveDialog(
                                                            key = "SelectArgumentType"
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
                        Text(
                            text = state.type.toString(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
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
                            onSubmit(state)
                            onDismiss()
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
}

