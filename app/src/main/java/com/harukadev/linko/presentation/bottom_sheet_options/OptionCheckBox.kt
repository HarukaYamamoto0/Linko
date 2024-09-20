package com.harukadev.linko.presentation.bottom_sheet_options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harukadev.linko.ui.theme.interFamily

@Preview
@Composable
fun  OptionCheckBox(
    modifier: Modifier = Modifier,
    text: String = "label",
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val defaultModifier = Modifier
        .fillMaxWidth()
        .clickable(
            onClick = { onCheckedChange(!checked) }
        )

    Row(
        modifier = defaultModifier.then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            Checkbox(
                checked = checked,
                onCheckedChange = { onCheckedChange(!checked) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = interFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}