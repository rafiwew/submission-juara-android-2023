package com.piwew.jetprogrammingapp.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.piwew.jetprogrammingapp.di.Injection
import com.piwew.jetprogrammingapp.helper.ViewModelFactory
import com.piwew.jetprogrammingapp.model.LanguageItem
import com.piwew.jetprogrammingapp.ui.common.UiState
import com.piwew.jetprogrammingapp.ui.components.LanguageListItem
import com.piwew.jetprogrammingapp.ui.components.SearchBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (String) -> Unit
) {
    val searchResult by viewModel.searchResult.collectAsState(initial = emptyList())
    val query by viewModel.query.collectAsState(initial = "")

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAllLanguages()
            }

            is UiState.Success -> {
                Column {
                    SearchBar(
                        query = query,
                        onQueryChange = { newQuery ->
                            viewModel.setQuery(newQuery)
                            viewModel.searchLanguages()
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                    )
                    HomeContent(
                        groupedLanguages = if (query.isEmpty()) uiState.data else emptyMap(),
                        searchResult = searchResult,
                        modifier = modifier,
                        navigateToDetail = navigateToDetail,
                    )
                }
            }

            is UiState.Error -> {}
        }
    }
}

@Composable
fun HomeContent(
    groupedLanguages: Map<Char, List<LanguageItem>>,
    searchResult: List<LanguageItem>,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
) {
    Box(
        modifier = modifier.padding(8.dp)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 80.dp),
            modifier = modifier.testTag("LanguageList")
        ) {
            if (searchResult.isNotEmpty()) {
                items(searchResult, key = { it.item.id }) { data ->
                    LanguageListItem(
                        name = data.item.name,
                        logoUrl = data.item.logoUrl,
                        modifier = Modifier.clickable {
                            navigateToDetail(data.item.id)
                        }
                    )
                }
            } else {
                groupedLanguages.entries.forEach { (_, languageItems) ->
                    items(languageItems) { data ->
                        LanguageListItem(
                            name = data.item.name,
                            logoUrl = data.item.logoUrl,
                            modifier = Modifier.clickable {
                                navigateToDetail(data.item.id)
                            }
                        )
                    }
                }
            }
        }
    }
}