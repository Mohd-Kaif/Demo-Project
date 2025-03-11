package com.example.starwars.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.viewModelFactory
import coil3.compose.AsyncImage
import com.example.starwars.BUFFER_SIZE

import com.example.starwars.R
import com.example.starwars.StarWarsTopAppBar
import com.example.starwars.data.CharacterData
import com.example.starwars.data.DataProvider
import com.example.starwars.imageUrl
import com.example.starwars.viewModel.HomeViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

const val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    navigateToCharacterDetails: (CharacterData) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()

    // State to track the scroll position
    val listState = rememberLazyListState()
    // Coroutine scope for handling background operations like loading data
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            StarWarsTopAppBar(
                title = stringResource(R.string.app_name),
                canNavigateBack = false,
                canShareDetails = false
            )
                 },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = stringResource(R.string.go_to_top)
                )
            }
        }
    ) { innerPadding ->
        HomeBody(
            itemList = homeUiState.itemList,
            loadMoreItems = { viewModel.getAllCharacterData() },
            listState = listState,
            isLoading = homeUiState.isLoading,
            onItemClick = navigateToCharacterDetails,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun HomeBody(
    itemList: List<CharacterData>,
    loadMoreItems: () -> Unit,
    listState: LazyListState,
    isLoading: Boolean,
    onItemClick: (CharacterData) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    buffer: Int = BUFFER_SIZE,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            // Get the total number of items in the list
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            // Get the index of the last visible item
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            // Check if we have scrolled near the end of the list and more items should be loaded
            lastVisibleItemIndex >= (totalItemsCount - buffer) && !isLoading
        }
    }

    LaunchedEffect(listState) { // When listState changes (due to scrolling), this will execute
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .filter { it } // Ensure that we load more items only when needed
            .collect{
                loadMoreItems()
            }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        state = listState
    ) {
        itemsIndexed(itemList, key = { _, item -> item.name }) { index, item ->
            CharacterCard(
                item = item,
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .clickable { onItemClick(item) }
            )
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        if (error != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun CharacterCard(
    item: CharacterData,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = imageUrl ,
                contentDescription = "Character Image",
                modifier = Modifier
                    .size(100.dp)
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
            Text(
                text = item.name,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = modifier
                    .padding(start = 16.dp)
                    .weight(2.0f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navigateToCharacterDetails = {})
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    HomeBody(
        itemList = DataProvider.characterList(),
        loadMoreItems = {},
        listState = LazyListState(),
        isLoading = false,
        onItemClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CharacterCardPreview() {
    CharacterCard(
        item = DataProvider.character()
    )
}