package com.piwew.jetprogrammingapp.ui.screen.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.piwew.jetprogrammingapp.R
import com.piwew.jetprogrammingapp.di.Injection
import com.piwew.jetprogrammingapp.helper.ViewModelFactory
import com.piwew.jetprogrammingapp.model.LanguageItem
import com.piwew.jetprogrammingapp.ui.common.UiState
import com.piwew.jetprogrammingapp.ui.components.LanguageListItem
import com.piwew.jetprogrammingapp.ui.theme.JetProgrammingAppTheme

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateBack: () -> Unit,
    navigateToDetail: (String) -> Unit
) {
    val favoriteLanguages by viewModel.favoriteLanguages.collectAsState(emptyList())

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAllFavoriteLanguages()
            }

            is UiState.Success -> {
                FavoriteContent(
                    favoriteLanguages = favoriteLanguages,
                    modifier = modifier,
                    onBackClick = navigateBack,
                    navigateToDetail = navigateToDetail
                )
            }

            is UiState.Error -> {}
        }
    }
}

@Composable
fun FavoriteContent(
    favoriteLanguages: List<LanguageItem>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Box {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.Black,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onBackClick() }
                )
                Text(
                    text = "Favorite",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    modifier = modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )
            }
        }
        Column {
            if (favoriteLanguages.isEmpty()) {
                Text(
                    text = stringResource(R.string.empty_favorite),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .testTag("EmptyFavoriteText"),
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(favoriteLanguages) { data ->
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

@Preview(showBackground = true)
@Composable
fun PreviewFavoriteContent() {
    JetProgrammingAppTheme {
        FavoriteContent(
            favoriteLanguages = listOf(),
            onBackClick = {},
            navigateToDetail = {}
        )
    }
}