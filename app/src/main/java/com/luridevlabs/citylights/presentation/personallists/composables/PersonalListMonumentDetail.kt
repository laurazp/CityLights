package com.luridevlabs.citylights.presentation.personallists.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.luridevlabs.citylights.presentation.MainActivity
import com.luridevlabs.citylights.presentation.utils.capitalizeLowercase
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel

/**
 * Debería haber reutilizado el MonumentDetail, pero al obtener los datos desde Room, no necesitaba
 * la gestión de estados, ni la obtención de detalles mediante una llamada a la API porque ya están
 * disponibles en el viewModel.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalListMonumentDetail(
    navController: NavController,
    monumentViewModel: MonumentsViewModel,
    monumentId: String
) {
    val context = LocalContext.current
    val selectedMonument = monumentViewModel.getSelectedPersonalList().monuments.first {
        it.monumentId == monumentId.toLong()
    }

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
                        IconButton(onClick = {
                            (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
                                true
                            )
                            navController.navigateUp()
                        }) {
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
            //InfoCard
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
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = selectedMonument.title,
                            modifier = Modifier
                                .padding(6.dp)
                                .weight(1f),
                            style = MaterialTheme.typography.titleLarge
                        )
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
            //MapCard
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
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
        )
    }
}
