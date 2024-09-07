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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.harukadev.linko.R
import com.harukadev.linko.presentation.bottom_sheet_options.OptionCheckBox
import com.harukadev.linko.presentation.bottom_sheet_options.OptionTextField
import com.harukadev.linko.ui.theme.LinkoTheme
import com.harukadev.linko.ui.theme.interFamily
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun App(viewModel: MainActivityViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(R.drawable.background),
                contentScale = ContentScale.FillWidth
            )
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.drawable.ic_app),
                modifier = Modifier.size(161.dp),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
            )

            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontFamily = interFamily,
                fontSize = 36.sp,
                modifier = Modifier
                    .padding(0.dp)
                    .padding(top = 10.dp, bottom = 90.dp),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = uiState.url,
                    modifier = Modifier.weight(1f),
                    onValueChange = { viewModel.setUrl(it) },
                    label = { Text(stringResource(R.string.label_textField_url)) },
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
                    textStyle = TextStyle(
                        fontFamily = interFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                    )
                )

                FilledIconButton(
                    onClick = {
                        val value = if (uiState.url == "") clipboardManager.getText().toString()
                        else ""

                        viewModel.setUrl(value)
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Icon(
                        painter =
                        if (uiState.url == "") painterResource(R.drawable.ic_clipboard_text)
                        else painterResource(
                            R.drawable.ic_clear
                        ), contentDescription = "", tint = MaterialTheme.colorScheme.background
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
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    fontFamily = interFamily
                )
            }

            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
                    .padding(bottom = 20.dp),
                onClick = {
                    viewModel.showBottomSheet()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
                ),
            ) {
                Text(
                    text = "Show advanced options",
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            if (uiState.showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                viewModel.showBottomSheet()
                            }
                        }
                    },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        OptionTextField(
                            value = uiState.urlShort,
                            title = "Short URL:",
                            placeholder = "myshortenedurl",
                            onValueChange = { viewModel.setUrlShort(it) }
                        )

                        OptionCheckBox(
                            text = "Create QR Code",
                            checked = uiState.optionQR,
                            onCheckedChange = { viewModel.setOptionQR(it) }
                        )

                        OptionCheckBox(
                            text = "Enable statistics",
                            checked = uiState.optionStatistics,
                            onCheckedChange = { viewModel.setOptionStatistics(it) }
                        )
                    }
                }
            }
        }
    }
}
