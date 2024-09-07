package com.harukadev.linko.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import com.harukadev.linko.api.Shortener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

@Serializable
data class MainUiState(
    var url: String = "",
    var shortenedUrl: String = "",
    var isLoading: Boolean = false,
    var showBottomSheet: Boolean = false,
    var urlShort: String = "",
    var optionQR: Boolean = false,
    var optionStatistics: Boolean = false
)

class MainScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun setUrl(url: String) {
        _uiState.update { currentState ->
            currentState.copy(
                url = url
            )
        }
    }

    suspend fun shortenUrl(url: String): Boolean {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }

        val shortener = Shortener()
        val shortenedUrl = shortener.shortenUrl(url)

        _uiState.update { currentState ->
            currentState.copy(
                shortenedUrl = shortenedUrl,
                isLoading = false
            )
        }

        return shortenedUrl.isNotBlank()
    }

    fun showBottomSheet() {
        _uiState.update { currentState ->
            currentState.copy(
                showBottomSheet = !_uiState.value.showBottomSheet
            )
        }
    }

    fun setUrlShort(urlShort: String) {
        _uiState.update { currentState ->
            currentState.copy(
                urlShort = urlShort
            )
        }
    }

    fun setOptionQR(boolean: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                optionQR = boolean
            )
        }
    }

    fun setOptionStatistics(boolean: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                optionStatistics = boolean
            )
        }
    }

    // https://ktor.io/docs/client-serialization.html#register_json
}