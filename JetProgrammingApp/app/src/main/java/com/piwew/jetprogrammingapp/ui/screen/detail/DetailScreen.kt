package com.piwew.jetprogrammingapp.ui.screen.detail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.piwew.jetprogrammingapp.R
import com.piwew.jetprogrammingapp.di.Injection
import com.piwew.jetprogrammingapp.helper.ViewModelFactory
import com.piwew.jetprogrammingapp.ui.common.UiState
import com.piwew.jetprogrammingapp.ui.components.PrimaryButton

@Composable
fun DetailScreen(
    languageId: String,
    viewModel: DetailViewModel = viewModel(factory = ViewModelFactory(Injection.provideRepository())),
    navigateBack: () -> Unit,
) {
    val isFavorite = remember { mutableStateOf(false) }

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getLanguageById(languageId)
            }

            is UiState.Success -> {
                val data = uiState.data

                viewModel.checkFavorite(languageId) { isLanguageFavorite ->
                    isFavorite.value = isLanguageFavorite
                }

                val openBrowser = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    when (result.resultCode) {
                        Activity.RESULT_OK -> {}
                    }
                }

                DetailContent(
                    photoUrl = data.item.photoUrl,
                    logoUrl = data.item.logoUrl,
                    name = data.item.name,
                    desc = data.item.desc,
                    createdBy = data.item.createdBy,
                    onBackClick = navigateBack,
                    isFavorite = isFavorite.value,
                    onToggleFavorite = {
                        if (isFavorite.value) {
                            viewModel.removeFromFavorites(languageId)
                            isFavorite.value = false
                        } else {
                            viewModel.addToFavorites(languageId)
                            isFavorite.value = true
                        }
                    },
                    onPrimaryButtonClicked = {
                        val intent =
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.google.com/search?q=${data.item.name}")
                            )
                        openBrowser.launch(intent)
                    }
                )
            }

            is UiState.Error -> {}
        }
    }
}

@Composable
fun DetailContent(
    photoUrl: String,
    logoUrl: String,
    name: String,
    desc: String,
    createdBy: String,
    onBackClick: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onPrimaryButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        tint = Color.Black,
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { onBackClick() }
                    )
                    IconButton(
                        onClick = { onToggleFavorite() },
                        modifier = modifier.padding(top = 4.dp)
                    ) {
                        val icon =
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                        Icon(
                            imageVector = icon,
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.Black,
                            contentDescription = stringResource(R.string.add_favorite),
                        )
                    }
                }
            }
            AsyncImage(
                model = logoUrl,
                contentDescription = "logo",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .height(400.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            ) {
                Text(
                    text = name,
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "Deskripsi",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left,
                    modifier = modifier
                        .padding(top = 4.dp)
                        .align(Alignment.Start)
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    modifier = modifier.padding(top = 4.dp)
                )
                Text(
                    text = "Dibuat oleh",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Left,
                    modifier = modifier
                        .padding(top = 16.dp, bottom = 4.dp)
                        .align(Alignment.Start)
                )
                Row(
                    modifier = modifier.align(Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "rafi_photo",
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Column {
                        Text(
                            text = createdBy,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = modifier
                                .padding(top = 4.dp)
                                .align(Alignment.Start)
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .padding(top = 16.dp)
                        .background(Color.LightGray)
                )
                Column(
                    modifier = modifier.padding(top = 16.dp)
                ) {
                    PrimaryButton(
                        text = "Cari tahu lebih lanjut",
                        onClick = { onPrimaryButtonClicked() }
                    )
                }
            }
        }
    }
}