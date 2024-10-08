package com.harukadev.linko.presentation.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

@Serializable
data class HomeScreenUiState(
    var url: String = "",
    var bottomSheetIsVisible: Boolean = false,
    var urlShort: String = "",
    var optionQR: Boolean = true,
    var optionStatistics: Boolean = true
)

class HomeScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    fun setUrl(url: String) {
        _uiState.update {
            it.copy(
                url = url
            )
        }
    }

    fun toggleBottomSheet() {
        _uiState.update {
            it.copy(
                bottomSheetIsVisible = !_uiState.value.bottomSheetIsVisible
            )
        }
    }

    fun setUrlShort(urlShort: String) {
        _uiState.update {
            it.copy(
                urlShort = urlShort
            )
        }
    }

    fun setOptionQR(boolean: Boolean) {
        _uiState.update {
            it.copy(
                optionQR = boolean
            )
        }
    }

    fun setOptionStatistics(boolean: Boolean) {
        _uiState.update {
            it.copy(
                optionStatistics = boolean
            )
        }
    }

    // https://ktor.io/docs/client-serialization.html#register_json
}