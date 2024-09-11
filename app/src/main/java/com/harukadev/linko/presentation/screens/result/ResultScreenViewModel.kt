package com.harukadev.linko.presentation.screens.result

import androidx.lifecycle.ViewModel
import com.harukadev.linko.api.Shortener
import com.harukadev.linko.api.Shortener.Companion.BASE_URL_FOR_STATISTICS
import com.harukadev.linko.utils.Promise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

data class ResultScreenUiState(
    val shortenedUrl: String? = null,
    val urlForStatistics: String? = null,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val error: Exception? = null,
)

class ResultScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ResultScreenUiState())
    val uiState: StateFlow<ResultScreenUiState> = _uiState.asStateFlow()

    private val shortener = Shortener()

    suspend fun shorten(url: String) {
        withContext(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = shortener.shortenUrl(url)) {
                is Promise.Success -> {
                    _uiState.update {
                        it.copy(
                            shortenedUrl = result.data.url,
                            urlForStatistics = BASE_URL_FOR_STATISTICS + result.data.url
                                .split("/")
                                .last()
                        )
                    }
                }

                is Promise.Error -> {
                    _uiState.update {
                        it.copy(
                            isError = true, error = result.exception
                        )
                    }
                }

                is Promise.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
