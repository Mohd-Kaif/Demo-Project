package com.example.starwars.view.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.starwars.R
import com.example.starwars.StarWarsTopAppBar
import com.example.starwars.data.CharacterData
import com.example.starwars.data.DataProvider
import com.example.starwars.imageUrl

@Composable
fun CharacterDetailScreen(
    character: CharacterData?,
    navigateBack: () -> Unit,
    shareDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            StarWarsTopAppBar(
                title = character?.name ?: "Character Details",
                canNavigateBack = true,
                canShareDetails = true,
                navigateUp = navigateBack,
                shareDetails = shareDetails
                )
        }
    ) { innerPadding ->
        if (character != null) {
            CharacterDetailBody(
                characterData = character,
                contentPadding = innerPadding,
            )
        }
//        else {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("Character details not available", style = MaterialTheme.typography.bodyLarge)
//            }
//        }
    }
}

@Composable
fun CharacterDetailBody(
    characterData: CharacterData,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .padding(16.dp)
            .padding(contentPadding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Character Image",
            modifier = modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
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
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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