package com.harukadev.linko.presentation.screens.result

import androidx.compose.foundation.background
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
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.placeholder
import com.eygraber.compose.placeholder.shimmer
import com.harukadev.linko.R
import com.harukadev.linko.presentation.screens.home.HomeRouter
import com.harukadev.linko.ui.theme.LinkoTheme
import com.harukadev.linko.ui.theme.interFamily
import com.lightspark.composeqr.QrCodeView
import kotlinx.serialization.Serializable

@Serializable
data class ResultRouter(
    var url: String,
    var urlShort: String? = null,
    var optionQR: Boolean = false,
    var optionStatistics: Boolean = false
)

@Composable
fun ResultScreen(
    navController: NavController = rememberNavController(),
    viewModel: ResultScreenViewModel = viewModel(),
    args: ResultRouter
) {
    val clipboardManager = LocalClipboardManager.current

    LinkoTheme {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(true) {
            viewModel.shorten(args.url, args.urlShort)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .paint(
                    painter = painterResource(R.drawable.bg_main),
                    contentScale = ContentScale.FillBounds
                )
                .padding(horizontal = 12.dp),
            verticalArrangement = if (args.optionQR) Arrangement.Top else Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (args.optionQR) {
                QrCodeView(
                    data = args.url,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .size(250.dp)
                        .background(Color.White)
                        .placeholder(
                            visible = uiState.isLoading,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp),
                            highlight = PlaceholderHighlight.shimmer(
                                highlightColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                            ),
                        )
                        .padding(10.dp)
                )
            }

            TextFieldWithTitleAndButton(
                modifier = Modifier.padding(bottom = 20.dp, top = 80.dp),
                title = "Shortened URL:",
                value = uiState.shortenedUrl,
                isLoading = uiState.isLoading,
                clipboardManager = clipboardManager,
            )

            if (args.optionStatistics) {
                TextFieldWithTitleAndButton(
                    title = "Url for statistics:",
                    value = uiState.urlForStatistics,
                    isLoading = uiState.isLoading,
                    clipboardManager = clipboardManager,
                )
            }

            if (uiState.isError) {
                navController.navigate(
                    HomeRouter(isError = true, errorMessage = uiState.error?.message.toString())
                )
            }
        }
    }
}

@Composable
fun TextFieldWithTitleAndButton(
    modifier: Modifier = Modifier,
    title: String,
    value: String?,
    isLoading: Boolean,
    clipboardManager: ClipboardManager,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            fontFamily = interFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(0.dp)
                .padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = value ?: "Nothing",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .placeholder(
                        visible = isLoading,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                        ),
                    ),
                onValueChange = { },
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.7f
                    ),
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    unfocusedTextColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                ),
                readOnly = true,
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = interFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                )
            )

            CopyButton(onClick = {
                clipboardManager.setText(AnnotatedString(value ?: ""))
            })
        }
    }
}

@Composable
private fun CopyButton(onClick: () -> Unit) {
    FilledIconButton(
        onClick = onClick,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(0.dp)
            .padding(bottom = 8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.copy),
            contentDescription = "",
        )
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun ResultScreenPreview() {
    ResultScreen(
        args = ResultRouter(url = "https://github.com/eygraber/compose-placeholder")
    )
}