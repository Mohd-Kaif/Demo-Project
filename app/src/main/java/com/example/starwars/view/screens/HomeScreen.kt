package com.example.starwars.view.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.example.starwars.R
import com.example.starwars.data.CharacterData
import com.example.starwars.viewModel.HomeViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

const val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    // State variable to hold the list of items
    Log.d(TAG, "Inside HomeScreen")
    var items by remember { mutableStateOf<List<CharacterData>>(emptyList()) }
    // State to track the scroll position
    val listState = rememberLazyListState()
    // Coroutine scope for handling background operations like loading data
    val coroutineScope = rememberCoroutineScope()
    // State to track if more items are being loaded
    var isLoading by remember { mutableStateOf(false) }

//    val newCharacterData by viewModel.newCharacterData.collectAsState()

    fun loadMoreItems() {
        Log.d(TAG, "load more items")
        coroutineScope.launch {
            isLoading = true

            viewModel.getAllCharacterData()

//            val newCharacterData = viewModel.newCharacterData
//            Log.d(TAG, "${viewModel.newCharacterData.value.size}")
            val newItems: List<CharacterData> = viewModel.newCharacterData.value
            Log.d(TAG, "newItems: ${newItems.size}")
            items = items + newItems

            isLoading = false
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = { StarWarsTopAppBar(stringResource(R.string.app_name)) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Go to top"
                )
            }
        }
    ) { innerPadding ->
        HomeBody(
            itemList = items,
            loadMoreItems = ::loadMoreItems,
            listState = listState,
            isLoading = isLoading,
            contentPadding = innerPadding,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarWarsTopAppBar(title: String) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
    )
}

@Composable
fun HomeBody(
    itemList: List<CharacterData>,
    loadMoreItems: () -> Unit,
    listState: LazyListState,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    buffer: Int = 2,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: HomeViewModel
) {
//    val allCharacterData by viewModel.allCharacterData.observeAsState()
    Log.d(TAG, "Inside Home Body")

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
                Log.d(TAG, "Callig for load more items")
                loadMoreItems()
                Log.d(TAG, "Returning after loading items")
            }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        state = listState
    ) {
        itemsIndexed(itemList, key = { _, item -> item }) { index, item ->
            Log.d(TAG, "itemsLoading")
            CharacterCard(item = item, modifier = modifier.padding(4.dp).fillMaxWidth())

            if (index == itemList.lastIndex && !isLoading) {
                loadMoreItems()
            }
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

    }



//    if (allCharacterData == null) {
//        Text("no Data fetched")
//    }
//    else {
//        LazyColumn(
//            modifier = Modifier
//        ) {
////    Log.d(TAG, "CharacterCard")
//            allCharacterData?.let {
//                items(it) { character ->
//                    CharacterCard(
//                        item = character,
//                        modifier = modifier
//                            .padding(4.dp)
//                            .fillMaxWidth()
//                    )
//                }
//            }
//
//
//        }
//    }

}

@Composable
fun CharacterCard(item: CharacterData, modifier: Modifier = Modifier) {
//    var isLoading by remember { mutableStateOf(false) }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
//            if (isLoading) {
//                ImageLoadingScreen()
//            }
//            else {
//                val sizeResolver = rememberConstraintsSizeResolver()
//                val painter = rememberAsyncImagePainter(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data("https://media.istockphoto.com/id/636208094/photo/tropical-jungle.jpg?s=1024x1024&w=is&k=20&c=Zyc6mQ-VrbJIVjPOhrdzKlr6CpUdpcqT__bPJHJemXI=")
//                        .build()
//                )
//
//                Image(painter = painter, contentDescription = null, modifier = Modifier.weight(1f).then(sizeResolver))
////                if (painter.state is AsyncImagePainter.State.Success) {
////                    Image(painter = painter, contentDescription = null)
////                }
                Image(
                    painter = rememberAsyncImagePainter(
                        "https://media.istockphoto.com/id/636208094/photo/tropical-jungle.jpg?s=1024x1024&w=is&k=20&c=Zyc6mQ-VrbJIVjPOhrdzKlr6CpUdpcqT__bPJHJemXI="
                    ), contentDescription = null,
                    modifier = Modifier.size(200.dp).weight(1f)
                )
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data("https://media.istockphoto.com/id/636208094/photo/tropical-jungle.jpg?s=1024x1024&w=is&k=20&c=Zyc6mQ-VrbJIVjPOhrdzKlr6CpUdpcqT__bPJHJemXI=")
//                        .crossfade(true)
//                        .build(),
//                    contentDescription = item.name,
//                    modifier = Modifier
//                        .size(128.dp)
//                        .weight(1.0f),
//                    alpha = 0.7f,
//                    onError = {isLoading = false},
//                    error = painterResource(R.drawable.ic_broken_image),
//                    onSuccess = { isLoading = false}
//                )
//            }
            Text(
                text = item.name,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = modifier
                    .padding(horizontal = 4.dp)
                    .weight(2.0f)
            )
        }
    }
}

@Composable
fun ImageLoadingScreen() {
    CircularProgressIndicator(
        modifier = Modifier
            .width(64.dp)
            .size(128.dp),
        color = MaterialTheme.colorScheme.tertiary,
        trackColor = MaterialTheme.colorScheme.tertiaryContainer
    )
}


//@Preview(showBackground = true)
//@Composable
//fun CharacterCardPreview() {
//    CharacterCard(CharacterData(1, "Luke Skywalker", "19 BBY", "Male", "172", "Blue", "Blond"))
//}