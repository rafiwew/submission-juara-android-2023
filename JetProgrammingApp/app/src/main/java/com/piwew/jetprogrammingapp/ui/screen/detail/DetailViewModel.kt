package com.piwew.jetprogrammingapp.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piwew.jetprogrammingapp.data.LanguageRepository
import com.piwew.jetprogrammingapp.model.LanguageItem
import com.piwew.jetprogrammingapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: LanguageRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<LanguageItem>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<LanguageItem>> get() = _uiState

    fun getLanguageById(languageId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getLanguageItemById(languageId))
        }
    }

    fun addToFavorites(languageId: String) {
        viewModelScope.launch {
            repository.addToFavorites(languageId)
        }
    }

    fun removeFromFavorites(languageId: String) {
        viewModelScope.launch {
            repository.removeFromFavorites(languageId)
        }
    }

    fun checkFavorite(languageId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isFavorite = repository.isFavorite(languageId)
            onResult(isFavorite)
        }
    }
}