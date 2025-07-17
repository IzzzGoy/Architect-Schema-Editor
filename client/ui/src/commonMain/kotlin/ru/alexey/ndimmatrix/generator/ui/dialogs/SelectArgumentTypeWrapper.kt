package ru.alexey.ndimmatrix.generator.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.alexey.ndimmatrix.generator.presentation.api.models.ArgumentType
import ru.alexey.ndimmatrix.generator.presentation.api.parameters.DialogWrapper

data class SelectArgumentTypeWrapper(
    val onSelect: (ArgumentType) -> Unit,
    val onDismiss: () -> Unit,
) : DialogWrapper {

    @Composable
    override fun Content() {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text("Выберите тип аргумента", style = MaterialTheme.typography.titleLarge)
            },
            text = {
                LazyColumn (
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ArgumentType.entries) { type ->
                        Button(
                            onClick = { onSelect(type) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = type.toString())
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Отмена")
                }
            }
        )
    }
}
