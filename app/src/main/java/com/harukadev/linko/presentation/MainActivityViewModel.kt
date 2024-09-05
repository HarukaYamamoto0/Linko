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

    // https://ktor.io/docs/client-serialization.html#register_json
}