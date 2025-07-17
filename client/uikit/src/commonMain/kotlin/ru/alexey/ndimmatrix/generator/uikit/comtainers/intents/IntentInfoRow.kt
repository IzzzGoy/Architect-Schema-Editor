package ru.alexey.ndimmatrix.generator.uikit.comtainers.intents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import ru.alexey.ndimmatrix.generator.uikit.buttons.ConfigHoverableIconButton

@Composable
fun ConfigIntentRow(
    modifier: Modifier = Modifier,
    intent: String
) {
    Row(
        modifier = modifier
            .shadow(3.dp, shape = CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = intent,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.weight(1f))

        ConfigHoverableIconButton(icon = Icons.Default.Edit) {

        }

        ConfigHoverableIconButton(icon = Icons.Default.Delete) {

        }
    }
}

