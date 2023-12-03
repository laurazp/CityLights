package com.luridevlabs.citylights.presentation.main.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.presentation.common.composables.ApiInfoAlertDialog
import com.luridevlabs.citylights.presentation.common.composables.CircularProgressBar
import com.luridevlabs.citylights.presentation.common.composables.ErrorAlertDialog
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MonumentList(
    navController: NavController
) {
    val monumentViewModel: MonumentsViewModel = koinViewModel()
    var monuments = monumentViewModel.monumentsPagingList.collectAsLazyPagingItems()
    val keyboardController = LocalSoftwareKeyboardController.current

    var isSearching by remember {
        mutableStateOf(false)
    }
    var searchString by remember {
        mutableStateOf("")
    }
    val filteredMonuments =
        monumentViewModel.getFilteredMonumentsByName(searchString).collectAsLazyPagingItems()

    val alphabeticallySortedMonuments =
        monumentViewModel.sortMonumentsByName().collectAsLazyPagingItems()

    var isFiltering by remember {
        mutableStateOf(false)
    }
    var showMenu by remember {
        mutableStateOf(false)
    }

    var showAPIInfoDialog by remember {
        mutableStateOf(false)
    }

    var needsToRefresh by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                title = {
                    if (!isSearching) {
                        Text(
                            text = "",
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    } else {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = searchString,
                            onValueChange = { newSearchString ->
                                searchString = newSearchString
                            },
                            label = { Text(stringResource(R.string.search_monument_text)) },
                            shape = RectangleShape,
                            maxLines = 1,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = { keyboardController?.hide() }
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                actions = {
                    // Search button
                    IconButton(onClick = {
                        isSearching = !isSearching
                        searchString = ""
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_icon_description),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    // Dropdown Menu button
                    IconButton(onClick = {
                        showMenu = !showMenu
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_filter_list_24),
                            contentDescription = stringResource(R.string.filter_icon_description),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.width(180.dp)
                    )
                    {
                        DropdownMenuItem(
                            text = { Text(text = "Sort by name") },
                            onClick = {
                                isFiltering = !isFiltering
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_sort_24),
                                    contentDescription = stringResource(R.string.filter_icon_description),
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Show API info") },
                            onClick = {
                                showAPIInfoDialog = !showAPIInfoDialog
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_info_24),
                                    contentDescription = stringResource(R.string.show_api_info),
                                )
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        if (showAPIInfoDialog) {
            ApiInfoAlertDialog(
                onDismissRequest = {
                    showAPIInfoDialog = false
                },
                dialogTitle = stringResource(R.string.api_info_title),
                dialogText = stringResource(R.string.api_info_description)
            )
        }

        if (needsToRefresh) {
            monuments = monumentViewModel.monumentsPagingList.collectAsLazyPagingItems()
            println("Refreshed!")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(color = MaterialTheme.colorScheme.background)
        ) {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                monuments.apply {
                    when {
                        loadState.refresh is LoadState.Loading ||
                                loadState.append is LoadState.Loading-> {
                            item { CircularProgressBar() }
                        }

                        loadState.refresh is LoadState.Error ||
                                loadState.append is LoadState.Error -> {
                            item {
                                ErrorAlertDialog(
                                    onConfirmation = {
                                        needsToRefresh = true
                                        println("Refreshed!")
                                    },
                                    dialogTitle = stringResource(R.string.list_error_title),
                                    dialogText = stringResource(R.string.list_error_text)
                                )
                            }
                        }
                    }
                }

                /**
                 * Lista de monumentos en función de si se está buscando por nombre,
                 * si se quiere ordenar los monumentos alfabéticamente por nombre
                 * o simplemente mostrar la lista completa de monumentos.
                 */
                val monumentList =
                    when {
                        isSearching -> filteredMonuments
                        isFiltering -> alphabeticallySortedMonuments
                        needsToRefresh -> monuments
                        else -> monuments
                    }

                items(
                    count = monumentList.itemCount,
                    key = monumentList.itemKey { monument: Monument -> monument.monumentId }
                ) { monumentIndex ->
                    monumentList[monumentIndex]?.let { item ->
                        MonumentListItem(
                            monument = item,
                            modifier = Modifier.fillMaxWidth()
                        ) { currentMonument ->
                            navController.navigate("monumentDetail/${currentMonument.monumentId}")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonumentListItem(
    monument: Monument,
    modifier: Modifier = Modifier,
    onClick: (Monument) -> Unit = {},
) {
    Card(
        modifier = modifier
            .padding(horizontal = 12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            hoveredElevation = 16.dp,
            pressedElevation = 20.dp
        ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        onClick = { onClick(monument) }
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.tertiary,
                            MaterialTheme.colorScheme.tertiaryContainer
                        )
                    )
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout(modifier = modifier.padding(8.dp)) {
                val (
                    nameView,
                    photoView,
                ) = createRefs()
                AsyncImage(
                    model = monument.image,
                    placeholder = painterResource(R.drawable.church_icon),
                    error = painterResource(R.drawable.church_icon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(70.dp)
                        .constrainAs(photoView) {
                            top.linkTo(parent.top, 4.dp)
                            bottom.linkTo(parent.bottom, 4.dp)
                            start.linkTo(parent.start, 4.dp)
                        }
                )
                Text(
                    text = monument.title,
                    modifier = Modifier
                        .constrainAs(nameView) {
                            top.linkTo(parent.top)
                            start.linkTo(photoView.end, 12.dp)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        },
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}
