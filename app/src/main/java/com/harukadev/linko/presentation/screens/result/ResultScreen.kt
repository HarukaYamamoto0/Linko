package com.harukadev.linko.presentation.screens.result

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.harukadev.linko.R
import com.harukadev.linko.ui.theme.LinkoTheme
import com.harukadev.linko.utils.ResultUtil
import kotlinx.serialization.Serializable

@Serializable
data class ResultRouter(
    var url: String = "",
    var urlShort: String = "",
    var optionQR: Boolean = false,
    var optionStatistics: Boolean = false
)

@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: ResultScreenViewModel = viewModel(),
    args: ResultRouter
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var result by remember { mutableStateOf<ResultUtil<String>?>(null) }

    LinkoTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(R.drawable.bg_main),
                    contentScale = ContentScale.FillWidth
                )
        ) { innerPadding ->
            LaunchedEffect(args.url) {
                result = viewModel.shortenUrl(args.url)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val res = result) {
                    is ResultUtil.Success -> {
                        Log.d("ResultScreen", "Shortened URL: ${res.data}")
                        Text(
                            text = "Shortened URL: ${res.data}",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    is ResultUtil.Error -> {
                        Log.e("ResultScreen", "Error shortening URL: ${res.exception.message}")
                        Text(
                            text = "Error: ${res.exception.message}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    else -> {
                        Log.d("ResultScreen", "Waiting for results...")
                        Text(
                            text = "Loading...",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (uiState.isLoading) {
                Dialog(onDismissRequest = {}) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(80.dp),
                                strokeWidth = 10.dp,
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "Just wait a second...",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize(Alignment.Center),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
