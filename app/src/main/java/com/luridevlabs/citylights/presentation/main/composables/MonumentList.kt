package com.luridevlabs.citylights.presentation.main.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.presentation.common.ResourceState
import com.luridevlabs.citylights.presentation.common.composables.CircularProgressBar
import com.luridevlabs.citylights.presentation.common.composables.ErrorAlertDialog
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MonumentList(
    navController: NavController
) {
    val monumentViewModel: MonumentsViewModel = koinViewModel()
    val monuments = monumentViewModel.monumentsPagingList.collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val selectedMonumentState by monumentViewModel.getMonumentDetailLiveData().observeAsState()

    var isSearching by remember {
        mutableStateOf(false)
    }
    var searchString by remember {
        mutableStateOf("")
    }
    val filteredMonuments = monumentViewModel.getFilteredMonumentsByName(searchString).collectAsLazyPagingItems()

    val alphabeticallySortedMonuments = monumentViewModel.sortMonumentsByName().collectAsLazyPagingItems()
    var isFiltering by remember {
        mutableStateOf(false)
    }
    var showMenu by remember {
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
                                onDone = {keyboardController?.hide()}
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.secondary),
                actions = {
                    IconButton(onClick = {
                        isSearching = !isSearching
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_icon_description),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
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
                        modifier = Modifier.width(180.dp))
                    {
                        DropdownMenuItem(
                            text = { Text(text = "Sort by name") },
                            onClick = { isFiltering = !isFiltering },
                            leadingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_sort_24),
                                    contentDescription = stringResource(R.string.filter_icon_description),)
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            monuments.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { CircularProgressBar() }
                    }
                    loadState.append is LoadState.Loading -> {
                        item { CircularProgressBar() }
                    }
                    loadState.refresh is LoadState.Error ||
                            loadState.append is LoadState.Error -> {
                        item { ErrorAlertDialog(
                            onConfirmation = {},
                            dialogTitle = stringResource(R.string.list_error_title),
                            dialogText = stringResource(R.string.list_error_text)
                        )
                        }
                    }
                }
            }


            when (selectedMonumentState) {
                is ResourceState.Loading -> {
                    //TODO: add progress bar
                }

                is ResourceState.Success -> {
                    val selectedMonument =
                        (selectedMonumentState as ResourceState.Success<Monument>).result


                }

                is ResourceState.Error -> {
                    //TODO: Mostrar mensaje de error
                }

                else -> {}
            }


            //TODO: Prueba ordenar por nombre ----------------------
            if(isFiltering) {
                items(
                    count = alphabeticallySortedMonuments.itemCount,
                    key = alphabeticallySortedMonuments.itemKey { monument: Monument -> monument.monumentId }
                ) { monumentIndex ->
                    alphabeticallySortedMonuments[monumentIndex]?.let { item ->
                        MonumentListItem(
                            monument = item,
                            modifier = Modifier.fillMaxWidth()
                        ) { currentMonument ->
                            scope.launch {
                                withContext(Dispatchers.Main) {
                                    navController.navigate("monumentDetail/${currentMonument.monumentId}")
                                }
                            }
                        }
                    }
                }
            }

            if (isSearching) {
                items(
                    count = filteredMonuments.itemCount,
                    key = filteredMonuments.itemKey { monument: Monument -> monument.monumentId }
                ) { monumentIndex ->
                    filteredMonuments[monumentIndex]?.let { item ->
                        MonumentListItem(
                            monument = item,
                            modifier = Modifier.fillMaxWidth()
                        ) { currentMonument ->
                            scope.launch {
                                withContext(Dispatchers.Main) {
                                    navController.navigate("monumentDetail/${currentMonument.monumentId}")
                                }
                            }
                        }
                    }
                }
            } else {
                items(
                    count = monuments.itemCount,
                    key = monuments.itemKey { monument -> monument.monumentId }
                ) { monumentIndex ->
                    monuments[monumentIndex]?.let { item ->
                        MonumentListItem(
                            monument = item,
                            modifier = Modifier.fillMaxWidth()
                        ) { currentMonument ->
                            scope.launch {
                                withContext(Dispatchers.Main) {
                                    //TODO: state para observar monument y que no navegue hasta que sea success
                                    //monumentViewModel.fetchMonument(currentMonument.monumentId.toString())
                                    navController.navigate("monumentDetail/${currentMonument.monumentId}")
                                }
                            }
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
    ElevatedCard(
        modifier = modifier
            .padding(horizontal = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        onClick = { onClick(monument) }
    ) {
        ConstraintLayout(modifier = modifier.padding(4.dp)) {
            val (
                nameView,
                photoView,
                favoriteView
            ) = createRefs()
            AsyncImage(
                model = monument.image,
                placeholder = painterResource(R.drawable.church_icon),
                error = painterResource(R.drawable.church_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
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
                        start.linkTo(photoView.end, 8.dp)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(favoriteView.start)
                        width = Dimension.fillToConstraints
                    },
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = ImageVector.vectorResource(
                    id =
                    if (monument.isFavorite)
                        R.drawable.baseline_favorite_24
                    else
                        R.drawable.baseline_favorite_border_24
                ),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .constrainAs(favoriteView) {
                        start.linkTo(nameView.end, 8.dp)
                        end.linkTo(parent.end)
                        bottom.linkTo(nameView.bottom)
                        width = Dimension.value(20.dp)
                        height = Dimension.fillToConstraints
                    }
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}
