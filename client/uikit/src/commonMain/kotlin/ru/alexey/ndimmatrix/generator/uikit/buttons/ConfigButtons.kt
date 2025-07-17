package ru.alexey.ndimmatrix.generator.uikit.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ConfigButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    ) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(),
        modifier = modifier
    ) {
        Text(text = text)
    }
}


@Composable
fun ConfigHoverableIconButton(icon: ImageVector, onClick: () -> Unit) {
    val iconInteractionSource = remember {
        MutableInteractionSource()
    }
    val hoveredIcon by iconInteractionSource.collectIsHoveredAsState()
    val clickedIcon by iconInteractionSource.collectIsPressedAsState()
    Box(
        modifier = Modifier
            .size(30.dp)
            .hoverable(iconInteractionSource)
            .shadow(
                elevation = if (hoveredIcon && !clickedIcon) {
                    3.dp
                } else {
                    0.dp
                },
                shape = CircleShape
            )
            .background(MaterialTheme.colorScheme.surface)
            .clickable(
                onClick = onClick,
                interactionSource = iconInteractionSource,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .size(18.dp),
        )
    }
}