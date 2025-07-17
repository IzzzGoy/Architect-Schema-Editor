package ru.alexey.ndimmatrix.generator.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.stringResource
import ru.alexey.ndimmatrix.generator.uikit.comtainers.intents.ConfigIntentRow
import schema_editor.client.ui.generated.resources.Res
import schema_editor.client.ui.generated.resources.intents_form

@Composable
fun IntentFormDialog(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,

    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 12.dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .heightIn(max = 600.dp)
                .widthIn(min = 400.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.intents_form),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall
            )
            var intents by remember {
                mutableStateOf(
                    listOf("Intent1", "Intent2")
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(intents) {
                    ConfigIntentRow(
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        intent = it
                    )
                }
            }
        }
    }
}
