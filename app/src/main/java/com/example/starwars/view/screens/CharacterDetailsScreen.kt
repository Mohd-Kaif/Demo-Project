package com.example.starwars.view.screens

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.starwars.R
import com.example.starwars.StarWarsTopAppBar
import com.example.starwars.data.CharacterData
import com.example.starwars.data.DataProvider
import com.example.starwars.imageUrl
import com.example.starwars.viewModel.HomeViewModel
import com.example.starwars.viewModel.Result

@Composable
fun CharacterDetailScreen(
    id: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val character = (homeUiState as Result.Success).data[id]
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        topBar = {
            StarWarsTopAppBar(
                title = character.name,
                canNavigateBack = true,
                canShareDetails = true,
                navigateUp = navigateBack,
                shareDetails = { viewModel.shareDetails(context = context, characterData = character) }
            )
        }
    ) { innerPadding ->
        CharacterDetailBody(
            characterData = character,
            contentPadding = innerPadding,
        )
    }
}

@Composable
fun CharacterDetailBody(
    characterData: CharacterData,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .padding(contentPadding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(R.string.character_image),
            modifier = modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        CharacterDetails(characterData)
    }
}

@Composable
fun CharacterDetails(
    characterData: CharacterData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ItemDetailsRow(
            labelResID = R.string.name,
            itemDetail = characterData.name,
        )
        ItemDetailsRow(
            labelResID = R.string.height,
            itemDetail = characterData.height,
        )
        ItemDetailsRow(
            labelResID = R.string.birth_year,
            itemDetail = characterData.birth_year,
        )
        ItemDetailsRow(
            labelResID = R.string.gender,
            itemDetail = characterData.gender,
        )
        ItemDetailsRow(
            labelResID = R.string.hair_color,
            itemDetail = characterData.hair_color,
        )
        ItemDetailsRow(
            labelResID = R.string.eye_color,
            itemDetail = characterData.eye_color,
        )
    }
}

@Composable
private fun ItemDetailsRow(
    @StringRes labelResID: Int, itemDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
        Text(stringResource(labelResID) + ": ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = itemDetail, fontSize = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterDetailBodyPreview() {
    CharacterDetailBody(
        DataProvider.character()
    )
}