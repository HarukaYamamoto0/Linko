package com.harukadev.linko.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.harukadev.linko.R
import com.harukadev.linko.ui.theme.LinkoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LinkoTheme {
                App()
            }
        }
    }
}

@Preview
@Composable
fun App(viewModel: MainActivityViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .paint(
                    painter = painterResource(R.drawable.background),
                    contentScale = ContentScale.FillWidth
                )
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.drawable.ic_app),
                modifier = Modifier
                    .size(161.dp)
                    .padding(bottom = 11.dp),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
            )

            Text(
                text = "Linko",
                modifier = Modifier
                    .padding(0.dp)
                    .padding(bottom = 121.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight(600),
                fontSize = 36.sp
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = uiState.url,
                    modifier = Modifier.weight(1f),
                    onValueChange = { viewModel.setUrl(it) },
                    label = { Text("You URL here") },
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        unfocusedTextColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    ),
                    maxLines = 1,
                    singleLine = true,
                )

                FilledIconButton(
                    onClick = {
                        val value =
                            if (uiState.url == "") clipboardManager.getText().toString()
                            else ""

                        viewModel.setUrl(value)
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Icon(
                        painter = if (uiState.url == "")
                            painterResource(R.drawable.ic_clipboard_text)
                        else painterResource(
                            R.drawable.ic_clear
                        ),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp),
                onClick = { scope.launch { viewModel.shortenUrl(uiState.url) } },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Shorten URL!!",
                    fontWeight = FontWeight(700),
                    color = MaterialTheme.colorScheme.background
                )
            }

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {},
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
                ),
            ) {
                Text(
                    text = "Show advanced options",
                    fontSize = 16.sp
                )
            }
        }
    }
}