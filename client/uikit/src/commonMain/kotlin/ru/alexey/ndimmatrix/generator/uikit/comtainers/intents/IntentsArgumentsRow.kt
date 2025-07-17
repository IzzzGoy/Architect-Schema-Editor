package ru.alexey.ndimmatrix.generator.uikit.comtainers.intents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.unit.dp
import ru.alexey.ndimmatrix.generator.uikit.buttons.ConfigHoverableIconButton
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun IntentsArgumentsRow(
    modifier: Modifier = Modifier,
    argId: Int,
    argName: String,
    argType: String,
    isNullable: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .shadow(
                elevation = 3.dp,
                shape = CircleShape
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
    ) {
        Text(
            text = argName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "type:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        val color = MaterialTheme.colorScheme.onSurface
        BasicText(
            text = argType,
            style = MaterialTheme.typography.bodySmall,
            color = ColorProducer { color }
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Checkbox(
            checked = isNullable,
            onCheckedChange = null
        )
        Text(
            text = "nullable",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        ConfigHoverableIconButton(icon = Icons.Default.Edit) {

        }

        ConfigHoverableIconButton(icon = Icons.Default.Delete) {

        }
    }
}