package com.example.starwars.view.screens

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.starwars.R
import com.example.starwars.StarWarsTopAppBar
import com.example.starwars.data.CharacterData

//object CharacterDetailsDestination: NavigationDestination {
//    override val route = "character_details"
//    override val title =
//}

@Composable
fun CharacterDetailScreen(
//    viewModel: CharacterDetailViewModel,
    character: CharacterData?,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            StarWarsTopAppBar(
                title = character?.name ?: "Character Details",
                canNavigateBack = true,
                navigateUp = navigateBack
                )
        }
    ) { innerPadding ->
        if (character != null) {
            CharacterDetailBody(
                characterData = character,
                contentPadding = innerPadding,
            )
        }
        else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Character details not available", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun CharacterDetailBody(
    characterData: CharacterData,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        modifier = modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ImageWithShareButton(characterData)
        Spacer(modifier = Modifier.height(16.dp))
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
            itemDetail = characterData.birthYear,
        )
        ItemDetailsRow(
            labelResID = R.string.gender,
            itemDetail = characterData.gender,
        )
        ItemDetailsRow(
            labelResID = R.string.hair_color,
            itemDetail = characterData.hairColor,
        )
        ItemDetailsRow(
            labelResID = R.string.eye_color,
            itemDetail = characterData.eyeColor,
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

@Composable
fun ImageWithShareButton(characterData: CharacterData) {
    val context = LocalContext.current
    Box(modifier = Modifier.size(400.dp)) {
        AsyncImage(
            model = "https://media.istockphoto.com/id/636208094/photo/tropical-jungle.jpg?s=1024x1024&w=is&k=20&c=Zyc6mQ-VrbJIVjPOhrdzKlr6CpUdpcqT__bPJHJemXI=",
            contentDescription = "Character Image",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = { shareDetails(context, characterData) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
        }
    }
}

private fun shareDetails(context: Context, characterData: CharacterData) {
    val shareText = "Name: ${characterData.name}, Height: ${characterData.height}cm, Gender: ${characterData.gender}, Birth Year: ${characterData.birthYear}, Eye Color: ${characterData.eyeColor}, Hair Color: ${characterData.hairColor}"
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterDetailBodyPreview() {
    CharacterDetailBody(
        CharacterData("Luke Skywalker", "19 BBY", "Male", "172", "Blue", "Blond")
    )
}