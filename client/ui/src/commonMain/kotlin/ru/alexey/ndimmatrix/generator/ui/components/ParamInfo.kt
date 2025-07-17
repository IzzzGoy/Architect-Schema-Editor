import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentModel
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentType
import ru.alexey.ndimmatrix.generator.ui.screens.ParamDefinition

@Composable
fun ParamEditorDrawer(
    param: ParamDefinition,
    onParamChange: (ParamDefinition) -> Unit,
    onClose: () -> Unit,
) {
    var editingField by remember { mutableStateOf<String?>(null) }
    var editedName by remember { mutableStateOf(param.name) }
    var editedDescription by remember { mutableStateOf(param.description) }
    var editedArgs by remember { mutableStateOf(param.args.toMutableList()) }

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .width(320.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        shadowElevation = 8.dp,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Param Info", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(Modifier.height(12.dp))

            // -- Name
            Text("Name", style = MaterialTheme.typography.labelMedium)
            if (editingField == "name") {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    trailingIcon = {
                        IconButton(onClick = {
                            editingField = null
                            onParamChange(param.copy(name = editedName))
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Save")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(param.name)
                    TextButton(onClick = { editingField = "name" }) {
                        Text("Edit")
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // -- Description
            Text("Description", style = MaterialTheme.typography.labelMedium)
            if (editingField == "desc") {
                OutlinedTextField(
                    value = editedDescription,
                    onValueChange = { editedDescription = it },
                    trailingIcon = {
                        IconButton(onClick = {
                            editingField = null
                            onParamChange(param.copy(description = editedDescription))
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Save")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(param.description)
                    TextButton(onClick = { editingField = "desc" }) {
                        Text("Edit")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Arguments", style = MaterialTheme.typography.labelMedium)

            LazyColumn {
                itemsIndexed(param.args) { index, arg ->
                    val isEditing = editingField == "arg_$index"
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(8.dp)
                    ) {
                        if (isEditing) {
                            var argName by remember { mutableStateOf(arg.name) }
                            var argType by remember { mutableStateOf(arg.type) }

                            OutlinedTextField(
                                value = argName,
                                onValueChange = { argName = it },
                                label = { Text("Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = argType.toString(),
                                onValueChange = { argType = ArgumentType.valueOf(it) },
                                label = { Text("Type") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                IconButton(onClick = {
                                    val updated = editedArgs.toMutableList().apply {
                                        set(index, ArgumentModel(index, argName, argType))
                                    }
                                    editedArgs = updated
                                    editingField = null
                                    onParamChange(param.copy(args = updated))
                                }) {
                                    Icon(Icons.Default.Check, contentDescription = "Save")
                                }
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(arg.name, style = MaterialTheme.typography.bodyMedium)
                                    Text(arg.type.toString(), style = MaterialTheme.typography.bodySmall)
                                }
                                Row {
                                    IconButton(onClick = { editingField = "arg_$index" }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                                    }
                                    IconButton(onClick = {
                                        val updated = editedArgs.toMutableList().apply {
                                            removeAt(index)
                                        }
                                        editedArgs = updated
                                        onParamChange(param.copy(args = updated))
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    val newArg = ArgumentModel(0, "arg${editedArgs.size + 1}", ArgumentType.STRING)
                    editedArgs += newArg
                    onParamChange(param.copy(args = editedArgs))
                },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Add Argument")
            }
        }
    }
}
