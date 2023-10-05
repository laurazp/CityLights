package com.luridevlabs.citylights.presentation.composables

import android.widget.Toast
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.data.monument.remote.model.Geometry
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonumentList(
    navController: NavController,
    viewModel: MonumentsViewModel
) {
    val monuments = viewModel.monumentsList.collectAsLazyPagingItems()
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
                    .height(50.dp),
                title = {
                    if (!isSearching) {
                        Text(
                            text = "",
                            color = MaterialTheme.colorScheme.onPrimary
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
                            maxLines = 1
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                actions = {
                    IconButton(onClick = { isSearching = !isSearching }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
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
                        navController,
                        monument = item,
                        modifier = Modifier.fillMaxWidth()
                    ) { currentMonument ->
                        scope.launch {
                            withContext(Dispatchers.Main) {
                                //TODO: borrar una vez que funcione !!!
                                Toast.makeText(
                                    context,
                                    "Monumento: ${currentMonument.title}",
                                    Toast.LENGTH_LONG
                                ).show()

                                //TODO: navegar al detail !!!
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
    //@PreviewParameter(MonumentPreviewParameter::class)
    navController: NavController, //TODO: hace falta???
    monument: Monument,
    modifier: Modifier = Modifier,
    onClick: (Monument) -> Unit = {}, //TODO: esto para qué???
) {
    ElevatedCard(
        modifier = modifier
            .padding(horizontal = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        onClick = { onClick(monument) }
    ) {
        ConstraintLayout(modifier = modifier.padding(4.dp)) {
            val (
                nameView,
                photoView
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
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}

class MonumentPreviewParameter : PreviewParameterProvider<Monument> {
    override val values: Sequence<Monument>
        get() = sequenceOf(
            Monument(
                monumentId = "1",
                title = "name bla bla",
                address = "status bla bla",
                style = "species bla bla",
                hours = "type bla bla",
                visitInfo = "gender bla bla",
                image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2Sm6WtxxfkDF52q2jEViT_m_TdoaEqui3ODP6lZcrMVlARZSYDwl_7y_tMbC9sqOOb-s&usqp=CAU",
                data = "",
                description = "",
                geometry = Geometry(listOf(0.0, 0.0)),
                pois = "",
                price = "",
            )
        )

}