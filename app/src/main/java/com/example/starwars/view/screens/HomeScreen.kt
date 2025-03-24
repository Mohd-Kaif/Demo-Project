package com.example.starwars.view.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.starwars.BUFFER_SIZE
import com.example.starwars.R
import com.example.starwars.StarWarsTopAppBar
import com.example.starwars.data.CharacterData
import com.example.starwars.data.DataProvider
import com.example.starwars.imageUrl
import com.example.starwars.viewModel.HomeViewModel
import com.example.starwars.viewModel.Result
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

const val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    navigateToCharacterDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

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
        when (homeUiState) {
            is Result.Loading -> {
                LoadingScreen(
                    isLoading = isLoading,
                    loadMoreItems = { viewModel.getAllCharacterData(false) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
            is Result.Success -> {
                HomeBody(
                    itemList = (homeUiState as Result.Success<List<CharacterData>>).data,
                    loadMoreItems = { viewModel.getAllCharacterData(false) },
                    refreshData = {viewModel.getAllCharacterData(true)},
                    listState = listState,
                    isLoading = isLoading,
                    onItemClick = navigateToCharacterDetails,
                    contentPadding = innerPadding
                )
            }
            is Result.Error -> {
                ErrorScreen(
                    retryAction = { viewModel.getAllCharacterData(false) },
                    message = (homeUiState as Result.Error).exception?.message,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBody(
    itemList: List<CharacterData>,
    loadMoreItems: () -> Unit,
    refreshData: () -> Unit,
    listState: LazyListState,
    isLoading: Boolean,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    buffer: Int = BUFFER_SIZE,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex >= (totalItemsCount - buffer) && !isLoading
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .filter { it }
            .collect{
                loadMoreItems()
            }
    }

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = refreshData,
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(dimensionResource(R.dimen.padding_medium)),
            state = listState
        ) {
            itemsIndexed(itemList, key = { _, item -> item.name }) { index, item ->
                CharacterCard(
                    item = item,
                    modifier = modifier
                        .padding(dimensionResource(R.dimen.padding_small))
                        .fillMaxWidth()
                        .clickable { onItemClick(index) }
                )
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
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
            modifier = modifier.padding(dimensionResource(R.dimen.padding_small))
        ) {
            AsyncImage(
                model = imageUrl ,
                contentDescription = stringResource(R.string.character_image),
                modifier = Modifier
                    .size(100.dp)
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
            Text(
                text = item.name,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Left,
                modifier = modifier
                    .padding(start = dimensionResource(R.dimen.padding_medium))
                    .weight(2.0f)
            )
        }
    }
}

@Composable
fun LoadingScreen(
    isLoading: Boolean,
    loadMoreItems: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        if (!isLoading) {
            loadMoreItems()
        }
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, message: String?, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message?: stringResource(R.string.loading_failed),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .align(Alignment.CenterHorizontally)
        )
        Button(onClick = retryAction) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    HomeBody(
        itemList = DataProvider.characterList(),
        loadMoreItems = {},
        refreshData = {},
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