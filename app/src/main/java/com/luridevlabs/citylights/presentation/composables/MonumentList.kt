package com.luridevlabs.citylights.presentation.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonumentList(
    navController: NavController
) {
    val monumentViewModel: MonumentsViewModel = koinViewModel()
    val monuments = monumentViewModel.monumentsList.collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isSearching by remember {
        mutableStateOf(false)
    }
    var searchString by remember {
        mutableStateOf("")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
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
                                .padding(end = 12.dp)
                                .fillMaxWidth(),
                            value = searchString,
                            onValueChange = { newSearchString ->
                                searchString = newSearchString
                            },
                            label = { Text("Search for a monument") },
                            shape = RectangleShape,
                            maxLines = 1
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
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary
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
                                navController.navigate("monumentDetail/${currentMonument.monumentId}")
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
    onClick: (Monument) -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier
            .padding(horizontal = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
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

class MonumentPreviewParameter : PreviewParameterProvider<Monument> {
    override val values: Sequence<Monument>
        get() = sequenceOf(
            Monument(
                monumentId = 123,
                title = "name bla bla",
                address = "status bla bla",
                style = "species bla bla",
                hours = "type bla bla",
                visitInfo = "gender bla bla",
                image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2Sm6WtxxfkDF52q2jEViT_m_TdoaEqui3ODP6lZcrMVlARZSYDwl_7y_tMbC9sqOOb-s&usqp=CAU",
                data = "",
                description = "",
                position = LatLng(0.0, 0.0),
                pois = "",
                price = "",
            )
        )

}