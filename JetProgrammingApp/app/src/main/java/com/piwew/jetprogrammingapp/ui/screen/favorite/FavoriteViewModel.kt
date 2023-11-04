package com.piwew.jetprogrammingapp.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piwew.jetprogrammingapp.data.LanguageRepository
import com.piwew.jetprogrammingapp.model.LanguageItem
import com.piwew.jetprogrammingapp.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: LanguageRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<LanguageItem>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<LanguageItem>>> get() = _uiState

    val favoriteLanguages: Flow<List<LanguageItem>> = repository.getFavoriteLanguages()

    fun getAllFavoriteLanguages() {
        viewModelScope.launch {
            repository.getFavoriteLanguages()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { favoriteLanguageItems ->
                    _uiState.value = UiState.Success(favoriteLanguageItems)
                }
        }
    }

}