package com.luridevlabs.citylights.presentation.common.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.R.drawable
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.presentation.common.ResourceState
import com.luridevlabs.citylights.presentation.common.ResourceState.Error
import com.luridevlabs.citylights.presentation.common.ResourceState.Loading
import com.luridevlabs.citylights.presentation.common.ResourceState.Success
import com.luridevlabs.citylights.presentation.utils.capitalizeLowercase
import org.koin.androidx.compose.koinViewModel
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonumentDetail(
    navController: NavController,
    monumentId: String
) {
    val monumentViewModel: MonumentsViewModel = koinViewModel()
    val context = LocalContext.current
    //val scope = rememberCoroutineScope()

    val selectedMonumentState by monumentViewModel.getMonumentDetailLiveData().observeAsState()
    monumentViewModel.fetchMonument(monumentId)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                    //.padding(bottom = 4.dp),
                title = { Text("") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.secondary),
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        when (selectedMonumentState) {
            is Loading -> {
                //TODO: add progress bar
                Timber.i("gfgfg")
            }

            is Success -> {
                val selectedMonument = (selectedMonumentState as Success<Monument>).result

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
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
                        ConstraintLayout(modifier = Modifier.padding(8.dp)) {
                            val (
                                photoView,
                                titleView,
                                descriptionView,
                                styleView,
                                addressView,
                                hoursView
                            //TODO: añadir todos los campos
                            ) = createRefs()

                            AsyncImage(
                                model = selectedMonument.image,
                                placeholder = painterResource(drawable.church_icon),
                                error = painterResource(drawable.church_icon),
                                contentDescription = selectedMonument.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .fillMaxWidth()
                                    .constrainAs(photoView) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                            )
                            Row (
                                modifier = Modifier
                                    .constrainAs(titleView) {
                                        top.linkTo(photoView.bottom, 8.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                //TODO: separar del título !!!
                                //horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = selectedMonument.title,
                                    modifier = Modifier
                                        .padding(6.dp),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                IconButton(
                                    onClick = {
                                        //TODO: Add favorite when onClick
                                        Toast.makeText(context, "Add to favorites!", Toast.LENGTH_LONG).show()
                                    },
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(
                                            id =
                                            if (selectedMonument.isFavorite)
                                                drawable.baseline_favorite_24
                                            else
                                                drawable.baseline_favorite_border_24
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(24.dp)
                                            .height(24.dp)
                                    )
                                }
                            }
                            Text(
                                text = selectedMonument.description,
                                modifier = Modifier
                                    .constrainAs(descriptionView) {
                                        top.linkTo(titleView.bottom, 8.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    },
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Justify
                            )
                            Row(
                                modifier = Modifier
                                    .constrainAs(styleView) {
                                        top.linkTo(descriptionView.bottom, 12.dp)
                                        start.linkTo(parent.start)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Estilo:  ",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = selectedMonument.style.capitalizeLowercase(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .constrainAs(addressView) {
                                    top.linkTo(styleView.bottom, 12.dp)
                                    start.linkTo(parent.start)
                                },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Dirección:  ",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = selectedMonument.address,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .constrainAs(hoursView) {
                                        top.linkTo(addressView.bottom, 12.dp)
                                        start.linkTo(parent.start)
                                    },
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "Horario:  ",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = selectedMonument.hours,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            //TODO: Add all the fields
                        }
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
                        Box(
                            modifier = Modifier
                                .padding(6.dp)
                                .clip(shape = RoundedCornerShape(12.dp))
                        )
                        {
                            DetailMapView(selectedMonument)
                        }
                    }

                }
            }
            is Error -> {
                //TODO: Mostrar mensaje de error
            }

            null -> TODO()
        }
    }
}

@Composable
fun DetailMapView(monument: Monument) {
    val markerPosition =
        monument.position
    //val context = LocalContext.current
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        uiSettings = MapUiSettings(zoomControlsEnabled = true),
        properties = MapProperties(
            mapType = MapType.HYBRID
        ),
        cameraPositionState = CameraPositionState(
            CameraPosition(
                monument.position,
                16f,
                0f,
                0f
            )
        )
    ) {
        //TODO: mostrar pin en el mapa
        Marker(
            state = rememberMarkerState(position = markerPosition),
            title = monument.title,
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
        )
    }
}