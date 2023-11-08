package com.luridevlabs.citylights.presentation.main.composables

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    //val scope = rememberCoroutineScope()

    var showDialog by remember {
        mutableStateOf(false)
    }

    //TODO: revisar doble parpadeo --> MonumentList
    monumentViewModel.fetchMonument(monumentId)
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.secondary),
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_icon_description),
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
                CircularProgressBar()
            }

            is Success -> {
                val selectedMonument = (selectedMonumentState as Success<Monument>).result
                var isFavorite by remember { mutableStateOf(selectedMonument.isFavorite) }

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
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedMonument.title,
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .weight(1f),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                /**
                                 * Add to personal list button
                                 */
                                IconButton(
                                    onClick = {
                                        showDialog = !showDialog
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
                                }
                                AddToPersonalListsDialog(
                                    onListClick = { list ->
                                        //TODO: añadir monumento a lista
                                        monumentViewModel.addMonumentToList(list, selectedMonument)
                                    },
                                    dialogTitle = stringResource(R.string.choose_list_to_add_text),
                                    personalLists = personalLists
                                )

                                /*(
                                    expanded = showDialog,
                                    lists = personalLists,
                                    onDismiss = { showDialog = false },
                                    onListClick = {
                                                  //TODO: Añadir monumento a esa lista
                                    },
                                    onAddListClick = { },
                                    dialogTitle = "Elige una lista para añadir el monumento"
                                )*/

                                /**
                                 * Add to favorites button
                                 */
                                IconButton(
                                    onClick = {
                                        isFavorite = !isFavorite
                                        selectedMonument.isFavorite = !selectedMonument.isFavorite

                                        if (!monumentViewModel.isMonumentInList(
                                                monumentViewModel.personalLists[0],
                                                selectedMonument
                                        )) {
                                            monumentViewModel.addMonumentToList(
                                                monumentViewModel.personalLists[0],
                                                selectedMonument)
                                        } else {
                                            monumentViewModel.removeMonumentFromList(
                                                monumentViewModel.personalLists[0],
                                                selectedMonument)
                                        }
                                    },
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(
                                            id =
                                            if (monumentViewModel.isMonumentInList(
                                                    monumentViewModel.personalLists[0],
                                                    selectedMonument))
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
                ErrorAlertDialog(
                    onConfirmation = { /*TODO*/ },
                    dialogTitle = "",
                    dialogText = "")
            }

            null -> {}

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
        Marker(
            state = rememberMarkerState(position = markerPosition),
            title = monument.title,
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
        )
    }
}