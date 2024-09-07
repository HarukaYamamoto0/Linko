package com.harukadev.linko.presentation

import androidx.lifecycle.ViewModel
import com.harukadev.linko.api.Shortener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MainActivityUiState(
    var url: String = "",
    var shortenedUrl: String = "",
    var showBottomSheet: Boolean = false,
    var urlShort: String = "",
    var optionQR: Boolean = false,
    var optionStatistics: Boolean = false
)

class MainActivityViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    fun setUrl(url: String) {
        _uiState.update { currentState ->
            currentState.copy(
                url = url
            )
        }
    }

    suspend fun shortenUrl(url: String) {
        val shortener = Shortener()
        val shortenedUrl = shortener.shortenUrl(url)

        _uiState.update { currentState ->
            currentState.copy(
                shortenedUrl = shortenedUrl
            )
        }
    }

    fun showBottomSheet() {
        _uiState.update { currentState ->
            currentState.copy(
                showBottomSheet = true
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