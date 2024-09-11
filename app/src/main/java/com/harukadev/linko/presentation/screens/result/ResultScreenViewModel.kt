package com.harukadev.linko.presentation.screens.result

import androidx.lifecycle.ViewModel
import com.harukadev.linko.api.Shortener
import com.harukadev.linko.utils.ResultUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

data class ResultScreenUiState(
    val shortenedUrl: String = "",
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String? = null
)

class ResultScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ResultScreenUiState())
    val uiState: StateFlow<ResultScreenUiState> = _uiState.asStateFlow()

    private val shortener = Shortener()

    suspend fun shortenUrl(url: String): ResultUtil<String> {
        return withContext(Dispatchers.IO) {
            try {
                val shortenedUrl = shortener.shortenUrl(url)

                _uiState.update {
                    it.copy(
                        shortenedUrl = shortenedUrl,
                        isLoading = false
                    )
                }

                ResultUtil.Success(shortenedUrl)
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isError = true,
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
                ResultUtil.Error(error)
            }
        }
    }
}