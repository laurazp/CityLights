package com.luridevlabs.citylights.presentation.main.composables

import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.luridevlabs.citylights.presentation.common.ResourceState.Error
import com.luridevlabs.citylights.presentation.common.ResourceState.Loading
import com.luridevlabs.citylights.presentation.common.ResourceState.Success
import com.luridevlabs.citylights.presentation.common.composables.CircularProgressBar
import com.luridevlabs.citylights.presentation.common.composables.ErrorAlertDialog
import com.luridevlabs.citylights.presentation.personallists.composables.AddToPersonalListsDialog
import com.luridevlabs.citylights.presentation.utils.capitalizeLowercase
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonumentDetail(
    navController: NavController,
    monumentViewModel: MonumentsViewModel,
    monumentId: String
) {
    val selectedMonumentState by monumentViewModel.getMonumentDetailLiveData().observeAsState()

    monumentViewModel.fetchPersonalLists()
    val personalLists = monumentViewModel.personalLists

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = { Text("") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_icon_description),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        when (selectedMonumentState) {
            is Loading -> {
                CircularProgressBar()
            }

            is Success -> {
                val selectedMonument = (selectedMonumentState as Success<Monument>).result
                var isFavorite by remember { mutableStateOf(selectedMonument.isFavorite) }
                var showDialog by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.colorScheme.tertiaryContainer
                                )
                            )
                        )
                ) {
                    // InfoCard
                    ElevatedCard(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        ConstraintLayout(modifier = Modifier.padding(8.dp)) {
                            val (
                                photoView,
                                titleView,
                                descriptionView,
                                styleView,
                                addressView,
                                hoursView
                            ) = createRefs()

                            AsyncImage(
                                model = selectedMonument.image,
                                placeholder = painterResource(drawable.church_icon),
                                error = painterResource(drawable.church_icon),
                                contentDescription = selectedMonument.title,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .constrainAs(photoView) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                            )
                            Row(
                                modifier = Modifier
                                    .constrainAs(titleView) {
                                        top.linkTo(photoView.bottom, 8.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedMonument.title,
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .weight(1f),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                // Add to personal list button
                                IconButton(
                                    onClick = {
                                        showDialog = true
                                    },
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(
                                            id = drawable.baseline_add_24
                                        ),
                                        contentDescription = stringResource(R.string.add_to_personal_list),
                                        modifier = Modifier
                                            .width(24.dp)
                                            .height(24.dp)
                                    )

                                    if (showDialog) {
                                        AddToPersonalListsDialog(
                                            onListClick = { list ->
                                                monumentViewModel.addMonumentToList(
                                                    list,
                                                    selectedMonument
                                                )
                                                showDialog = false
                                            },
                                            onDismissClick = {
                                                showDialog = false
                                            },
                                            dialogTitle = stringResource(R.string.choose_list_to_add_text),
                                            personalLists = personalLists
                                        )
                                    }
                                }
                                // Add to favorites button
                                IconButton(
                                    onClick = {
                                        if (selectedMonument.isFavorite) {
                                            isFavorite = false
                                            monumentViewModel.removeMonumentFromList(
                                                monumentViewModel.personalLists[0],
                                                selectedMonument
                                            )
                                        } else {
                                            selectedMonument.isFavorite = true
                                            isFavorite = true
                                            monumentViewModel.addMonumentToList(
                                                monumentViewModel.personalLists[0],
                                                selectedMonument
                                            )
                                        }
                                    },
                                ) {
                                    FavoriteIcon(isFavorite)
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
                                    text = stringResource(R.string.monument_style_title),
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
                                    text = stringResource(R.string.monument_address_title),
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
                                    text = stringResource(R.string.monument_hours_title),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = selectedMonument.hours,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(4.dp)
                    )
                    // MapCard
                    ElevatedCard(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                            .height(350.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
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
                ErrorAlertDialog(
                    onConfirmation = {
                        monumentViewModel.fetchMonument(monumentId)
                    },
                    dialogTitle = stringResource(R.string.errorTitle),
                    dialogText = stringResource(R.string.error_loading_detail_text)
                )
            }

            null -> {}
        }
        LaunchedEffect(key1 = "loading") {
            monumentViewModel.fetchMonument(monumentId)
        }
    }
}

@Composable
fun DetailMapView(monument: Monument) {
    val markerPosition =
        monument.position
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
        Marker(
            state = rememberMarkerState(position = markerPosition),
            title = monument.title,
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)
        )
    }
}

@Composable
fun FavoriteIcon(isFavorite: Boolean) {
    Icon(
        imageVector = ImageVector.vectorResource(
            id = if (isFavorite)
                drawable.baseline_favorite_24
            else
                drawable.baseline_favorite_border_24
        ),
        contentDescription = stringResource(R.string.add_to_favorites),
        modifier = Modifier
            .width(24.dp)
            .height(24.dp)
    )
}
