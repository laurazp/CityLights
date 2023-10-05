package com.luridevlabs.citylights.presentation.composables

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MonumentDetail(
    monumentId: String,
    viewModel: MonumentsViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val selectedMonument = remember {
        //TODO: cómo recuperar el monumento seleccionado a partir del id???
        //viewModel.fetchMonument(monumentId)
        //viewModel.selectedMonument
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        ElevatedCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            ConstraintLayout(modifier = Modifier.padding(4.dp)) {
                val (
                    photoView,
                    titleView,
                    descriptionView,
                    hoursView,
                //TODO: añadir el resto de info
                ) = createRefs()
                /*AsyncImage(
                    model = monument.image,
                    placeholder = painterResource(R.drawable.church_icon),
                    error = painterResource(R.drawable.church_icon),
                    contentDescription = monument.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .size(70.dp)
                        .constrainAs(photoView) {
                            top.linkTo(parent.top, 4.dp)
                            bottom.linkTo(parent.bottom, 4.dp)
                            start.linkTo(parent.start, 4.dp)
                        }
                )*/
                Text(
                    text = monumentId,
                    modifier = Modifier
                        .constrainAs(titleView) {
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
                /*Text(
                    text = monument.description,
                    modifier = Modifier.constrainAs(descriptionView) {
                        top.linkTo(photoView.top)
                        start.linkTo(photoView.end, 16.dp)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                    style = MaterialTheme.typography.titleMedium
                )
                Row(modifier = Modifier.constrainAs(hoursView) {
                    top.linkTo(photoView.top)
                    start.linkTo(photoView.end, 16.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },) {
                    Text(text = "Hours",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = monument.hours ?: "",
                        style = MaterialTheme.typography.titleSmall
                    )
                }*/

                /*Icon(
                    imageVector = ImageVector.vectorResource(
                        id =
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
                    modifier = Modifier.constrainAs(genderView) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.value(64.dp)
                        height = Dimension.value(64.dp)
                    }
                )*/
            }
        }
    }
}

suspend fun getMonument(monumentId: String, context: Context, viewModel: MonumentsViewModel) {
    viewModel.monumentsList.first {
        monumentId == monumentId
    }
}
