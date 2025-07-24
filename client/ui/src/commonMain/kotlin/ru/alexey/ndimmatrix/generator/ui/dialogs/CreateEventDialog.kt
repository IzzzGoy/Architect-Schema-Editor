package ru.alexey.ndimmatrix.generator.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.EventModel
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogWrapper
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsIntents
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogsParameterHolder
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.ParametersHolder

data class CreateEventDialogWrapper(
    val onSubmit: (EventModel) -> Unit,
    val onDismiss: () -> Unit,
) : DialogWrapper {

    companion object {
        const val KEY = "CreateEventDialogWrapper"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var eventName by remember { mutableStateOf("") }
        var eventDescription by remember { mutableStateOf("") }
        var relatedParameter by remember { mutableStateOf<String?>(null) }
        val dialogs = koinInject<DialogsParameterHolder>()
        val parametersHolder = koinInject<ParametersHolder>()

        var args by remember { mutableStateOf(emptyList<ArgumentModel>()) }

        val parameters by parametersHolder.flow.collectAsState()
        val coroutineScope = rememberCoroutineScope()

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
                            text = "Create Event",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                        )
                    }
                )

                // Поля ввода имени и описания
                TextField(
                    value = eventName,
                    onValueChange = {
                        eventName = it
                    },
                    prefix = { Text("Event name:") },
                    // ... остальные параметры как в CreateParameterDialogWrapper
                )

                TextField(
                    value = eventDescription,
                    onValueChange = {
                        eventDescription = it
                    },
                    prefix = { Text("Event description:") },
                    // ... остальные параметры
                )

                // Выбор связанного параметра
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Related parameter:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    ElevatedButton(
                        onClick = {
                            relatedParameter = null
                        },
                        enabled = relatedParameter != null
                    ) {
                        Text("Global event")
                    }

                    DropdownMenuButton(
                        items = parameters.nodes,
                        selectedItem = parameters.nodes.find { it.name == relatedParameter },
                        onItemSelected = { param ->
                            relatedParameter = param.name
                        },
                        itemText = { it.name },
                        label = "Select parameter"
                    )
                }

                // Список аргументов
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(args) { arg ->
                        ListItem(
                            headlineContent = { Text(arg.name) }
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
                                                    args += it
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
                            Icon(Icons.Default.Add, contentDescription = "Add argument")
                        }
                    }
                }

                // Кнопка создания
                ElevatedButton(
                    onClick = {
                        onSubmit(
                            EventModel(
                                name = eventName,
                                description = eventDescription,
                                args = args,
                                parameter = relatedParameter
                            )
                        )
                        onDismiss()
                    },
                    enabled = eventName.isNotBlank()
                ) {
                    Text("Create Event")
                }
            }
        }
    }
}



@Composable
fun <T> DropdownMenuButton(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemText: @Composable (T) -> String,
    label: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        // Кнопка, открывающая меню
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = selectedItem?.let { itemText(it) } ?: label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selectedItem != null) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            Icon(
                imageVector = if (expanded) {
                    Icons.Filled.ArrowDropUp
                } else {
                    Icons.Filled.ArrowDropDown
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Выпадающее меню
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = itemText(item),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}