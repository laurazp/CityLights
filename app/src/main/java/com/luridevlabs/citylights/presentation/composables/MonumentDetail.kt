package com.luridevlabs.citylights.presentation.composables

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.luridevlabs.citylights.model.Monument
import org.koin.androidx.compose.koinViewModel
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonumentDetail(
    navController: NavController,
    monumentId: String
) {
    val monumentViewModel: MonumentsViewModel = koinViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()



    //TODO: var selectedMonument = monumentViewModel.monumentDetailMutableLiveData.

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .height(32.dp)
                    .padding(top = 6.dp),
                title = { Text("") },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    } else {
                        null
                    }
                }
            )
        }
    ) { paddingValues -> //TODO: modificar paddingValues?
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                //.padding(horizontal = 6.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            //InfoCard
            ElevatedCard(
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.White, shape = RectangleShape)
                        .fillMaxWidth()
                        .height(500.dp)
                )
                Text(
                    text = "",
                    modifier = Modifier
                        .padding(6.dp),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
                /*Text(
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
                )*/
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
            Spacer(
                modifier = Modifier
                    .height(4.dp)
            )
            //MapCard
            ElevatedCard(
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth()
                    .height(350.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
            ) {
                Box(modifier = Modifier
                    .padding(6.dp))
                {
                    Box(modifier = Modifier
                        .clip(shape = RoundedCornerShape(12.dp)))
                    {
                        MapView()
                    }
                }
            }
        }
    }
}

@Composable
fun MapView() {
    val context = LocalContext.current
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        uiSettings = MapUiSettings(zoomControlsEnabled = true),
        properties = MapProperties(
            mapType = MapType.HYBRID
        ),
        cameraPositionState = CameraPositionState(
            CameraPosition(
                LatLng(41.65, -0.877), 16f, 0f, 0f)
        )
    )
}
