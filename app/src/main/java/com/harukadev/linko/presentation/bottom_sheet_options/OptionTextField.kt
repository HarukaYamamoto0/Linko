package com.harukadev.linko.presentation.bottom_sheet_options

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harukadev.linko.ui.theme.interFamily

@Preview(showBackground = true)
@Composable
fun TextFieldWithTitle(
    modifier: Modifier = Modifier,
    modifierTitle: Modifier = Modifier,
    modifierTextField: Modifier = Modifier,
    title: String = "Title",
    label: String = "Label",
    value: String = "Value",
    placeholder: String = "Placeholder",
    onValueChange: (String) -> Unit = {}
) {
    Column(modifier = modifier) {
        val defaultModifierTitle = Modifier
            .padding(0.dp)
            .padding(bottom = 8.dp)

        Text(
            text = title,
            fontFamily = interFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = defaultModifierTitle.then(modifierTitle)
        )

        val defaultModifierTextField = Modifier.fillMaxWidth()

        OutlinedTextField(
            value = value,
            modifier = defaultModifierTextField.then(modifierTextField),
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                unfocusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                focusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                focusedTextColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                unfocusedTextColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            ),
            maxLines = 1,
            singleLine = true,
        )
    }
}
