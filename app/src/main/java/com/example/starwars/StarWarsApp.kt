package com.example.starwars

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.starwars.view.navigation.StarWarsNavHost

@Composable
fun StarWarsApp(navController: NavHostController = rememberNavController()) {
    StarWarsNavHost(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarWarsTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    canShareDetails: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    shareDetails: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            TopAppBarButton(
                enabled = canNavigateBack,
                onClick = navigateUp,
                imageVec = Icons.AutoMirrored.Filled.ArrowBack,
                iconDescription = R.string.back_button
            )
        },
        actions = {
            TopAppBarButton(
                enabled = canShareDetails,
                onClick = shareDetails,
                imageVec = Icons.Filled.Share,
                iconDescription = R.string.share
            )
        }
    )
}

@Composable
fun TopAppBarButton(
    enabled: Boolean,
    onClick: () -> Unit,
    imageVec: ImageVector,
    @StringRes iconDescription: Int,
) {
    if (enabled) {
        IconButton(
            onClick = onClick,
            colors = IconButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onTertiary,
                disabledContentColor = Color.DarkGray,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Icon(
                imageVector = imageVec,
                contentDescription = stringResource(iconDescription),
            )
        }
    }
}
