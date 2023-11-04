package com.piwew.jetprogrammingapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piwew.jetprogrammingapp.data.LanguageRepository
import com.piwew.jetprogrammingapp.model.LanguageItem
import com.piwew.jetprogrammingapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: LanguageRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<Map<Char, List<LanguageItem>>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Map<Char, List<LanguageItem>>>> get() = _uiState

    private val _searchResult = MutableStateFlow<List<LanguageItem>>(emptyList())
    val searchResult: StateFlow<List<LanguageItem>> get() = _searchResult

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> get() = _query

    fun getAllLanguages() {
        viewModelScope.launch {
            repository.getSortedAndGroupedLanguage()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { groupedLanguageItems ->
                    _uiState.value = UiState.Success(groupedLanguageItems)
                }
        }
    }

    fun searchLanguages() {
        val currentQuery = _query.value
        viewModelScope.launch {
            repository.searchLanguages(currentQuery)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { searchResult ->
                    _searchResult.value = searchResult
                }
        }
    }

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }
}