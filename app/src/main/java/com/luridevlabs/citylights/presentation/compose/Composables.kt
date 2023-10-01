package com.luridevlabs.citylights.presentation.compose

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.model.Geometry
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonumentsList(
    viewModel: MonumentsViewModel /*= koinViewModel()*/
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

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    if (!isSearching) {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else{
                        TextField(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .fillMaxWidth(),
                            value = searchString,
                            onValueChange = { newSearchString ->
                                searchString = newSearchString
                            },
                            label = { Text ("Cadena de Búsqueda") },
                            maxLines =  1
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
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ){
            items(
                count = monuments.itemCount,
                key = monuments.itemKey { monument -> monument.monumentId }
            ){ monumentIndex ->
                monuments[monumentIndex]?.let { item ->
                    MonumentItem(
                        monument = item,
                        modifier = Modifier.fillMaxWidth()
                    ){ currentMonument ->
                        scope.launch {
                            withContext(Dispatchers.Main){
                                Toast.makeText(context, "Monumento: ${currentMonument.title}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MonumentItem(
    @PreviewParameter(MonumentPreviewParameter::class)
    monument: Monument,
    modifier: Modifier = Modifier,
    onClick: (Monument) -> Unit = {}
){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        onClick = { onClick(monument) }
    ) {
        ConstraintLayout(modifier = modifier.padding(4.dp)) {
            val (
                nameView,
                genderView,
                speciesView,
                photoView,
            ) = createRefs()

            Text(
                text = monument.title,
                modifier = Modifier.constrainAs(nameView){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                style = MaterialTheme.typography.titleLarge
            )
            AsyncImage(
                model = monument.image,
                contentDescription = null,
                modifier = Modifier.constrainAs(photoView){
                    top.linkTo(nameView.bottom, 4.dp)
                    bottom.linkTo(parent.bottom, 4.dp)
                }
            )
            /*Text(
                text = monument.description,
                modifier = Modifier.constrainAs(speciesView){
                    top.linkTo(photoView.top)
                    start.linkTo(photoView.end, 16.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                imageVector = ImageVector.vectorResource(id =
                if (character.gender.lowercase() == "male")
                    R.drawable.ic_male
                else
                    R.drawable.ic_female
                ),
                contentDescription = null,
                tint = if (character.gender.lowercase() == "male")
                    Color.Blue
                else
                    Color.Magenta,
                modifier = Modifier.constrainAs(genderView){
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.value(64.dp)
                    height = Dimension.value(64.dp)
                }
            )*/
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}

class MonumentPreviewParameter: PreviewParameterProvider<Monument>{
    override val values: Sequence<Monument>
        get() = sequenceOf(
            Monument(
                monumentId = 1,
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