package com.harukadev.linko.presentation.screens.result

import androidx.lifecycle.ViewModel
import com.harukadev.linko.service.ShortenURL
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
    val error: String? = null
)

class ResultScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ResultScreenUiState())
    val uiState: StateFlow<ResultScreenUiState> = _uiState.asStateFlow()

    private val shortener = ShortenURL()

    suspend fun shorten(url: String, surname: String? = null) {
        withContext(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }

            val result = shortener.shorten(url, surname)

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        shortenedUrl = result.getOrNull(),
                        urlForStatistics = shortener.getUrlForStatistics(result.getOrNull() ?: "")
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isError = true,
                        error = shortener.getErrorMessage(result.exceptionOrNull()?.message ?: "how strange..., I think this is an easter egg, but the bad kind.")
                    )
                }
            }
        }

        shortener.closeClient()
    }
}
