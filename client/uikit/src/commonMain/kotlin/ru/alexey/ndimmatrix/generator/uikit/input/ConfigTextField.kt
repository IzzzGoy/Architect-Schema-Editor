package ru.alexey.ndimmatrix.generator.uikit.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun ConfigTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isNumeric: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = if (isNumeric) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
    )
}

@Composable
fun ConfigTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isNumeric: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = if (isNumeric) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
    )
}

