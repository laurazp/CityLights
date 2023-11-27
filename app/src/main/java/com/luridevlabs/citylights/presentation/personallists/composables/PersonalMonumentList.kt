package com.luridevlabs.citylights.presentation.personallists.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.presentation.MainActivity
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalMonumentList(
    navController: NavController,
    monumentViewModel: MonumentsViewModel
) {
    val context = LocalContext.current
    val personalListId = monumentViewModel.selectedListPosition
    val listTitle = monumentViewModel.personalLists[personalListId].listName
    val monuments = monumentViewModel.personalLists[personalListId].monuments
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                title = {
                    Text(
                        text = listTitle,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.secondary),
                actions = {}
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(
                monuments
            ) { item ->
                MonumentListItem(
                    monument = item,
                    modifier = Modifier.fillMaxWidth()
                ) { currentMonument ->
                    (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    navController.navigate("monumentDetail/${currentMonument.monumentId}")
                }
            }
        }
    }

    BackHandler {
        scope.launch {
            (context as MainActivity).navigateTo(-1)
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
                        start.linkTo(photoView.end, 8.dp)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(favoriteView.start)
                        width = Dimension.fillToConstraints
                    },
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = ImageVector.vectorResource(
                    id =
                    if (monument.isFavorite) R.drawable.baseline_favorite_24
                    else R.drawable.baseline_favorite_border_24
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
