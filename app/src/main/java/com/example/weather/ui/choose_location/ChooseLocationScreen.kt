package com.example.weather.ui.choose_location

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.weather.R
import com.example.weather.domain.model.Coordinates
import com.example.weather.domain.model.Location
import com.example.weather.ui.theme.WeatherAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChooseLocationScreenRoute(
    viewModel: ChooseLocationViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onPermissionsChanged(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    ChooseLocationScreen(
        uiState = uiState,
        detectLocation = viewModel::detectLocation,
        onPermissionDenied = viewModel::onPermissionDenied,
        onPermissionsChanged = viewModel::onPermissionsChanged
    )
}

@Composable
fun ChooseLocationScreen(
    modifier: Modifier = Modifier,
    uiState: ChooseLocationUiState,
    detectLocation: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionsChanged: (Boolean) -> Unit
) {
    val location = uiState.location
    val title = when {
        location != null -> location.displayName
            ?: stringResource(R.string.location_current_position)

        uiState.isLoading -> stringResource(R.string.location_loading_title)
        !uiState.hasPreciseLocationPermission -> stringResource(R.string.location_permission_title)
        else -> stringResource(R.string.location_ready_title)
    }
    val description = when {
        uiState.isLoading -> stringResource(R.string.location_loading_description)
        !uiState.hasPreciseLocationPermission -> stringResource(R.string.location_permission_description)
        location != null -> stringResource(R.string.location_update_description)
        else -> stringResource(R.string.location_ready_description)
    }
    val actionLabel = when {
        uiState.isLoading -> stringResource(R.string.location_loading_title)
        !uiState.hasPreciseLocationPermission -> stringResource(R.string.location_allow_access)
        uiState.error != null -> stringResource(R.string.location_retry)
        location != null -> stringResource(R.string.location_update)
        else -> stringResource(R.string.location_detect)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.location_screen_title),
                modifier = Modifier.semantics { heading() },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.location_screen_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { liveRegion = LiveRegionMode.Polite },
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                trackColor = MaterialTheme.colorScheme.outlineVariant,
                                strokeWidth = 3.dp
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.location_on_24),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (location != null) {
                        Text(
                            text = stringResource(R.string.location_selected_label),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (!uiState.isLoading && uiState.error != null) {
                val message = when (uiState.error) {
                    ChooseLocationError.PRECISE_PERMISSION_REQUIRED ->
                        stringResource(R.string.location_error_precise_permission_required)
                    ChooseLocationError.LOCATION_UNAVAILABLE ->
                        stringResource(R.string.location_error_unavailable)
                }
                LocationErrorMessage(message = message)
            }

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { permission ->
                val fineGranted = permission[Manifest.permission.ACCESS_FINE_LOCATION] == true
                onPermissionsChanged(fineGranted)
                if (fineGranted) {
                    detectLocation()
                } else {
                    onPermissionDenied()
                }
            }

            val context = LocalContext.current
            val permissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            Button(
                onClick = {
                    val fineGranted = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    onPermissionsChanged(fineGranted)
                    if (fineGranted) {
                        detectLocation()
                    } else {
                        permissionLauncher.launch(permissions)
                    }
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = actionLabel,
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

@Composable
private fun LocationErrorMessage(message: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(R.drawable.error_24),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private val previewLocation = Location(
    displayName = "Moscow",
    coordinates = Coordinates(latitude = 55.7512, longitude = 37.6184),
    detectedTimeMillis = null
)

@Composable
private fun ChooseLocationPreview(uiState: ChooseLocationUiState) {
    WeatherAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ChooseLocationScreen(
                uiState = uiState,
                detectLocation = {},
                onPermissionDenied = {},
                onPermissionsChanged = {}
            )
        }
    }
}

@Composable
@Preview(name = "Permission required", showBackground = true)
fun ChooseLocationScreenPreview() {
    ChooseLocationPreview(ChooseLocationUiState())
}

@Composable
@Preview(name = "Ready to locate", showBackground = true)
private fun ReadyChooseLocationScreenPreview() {
    ChooseLocationPreview(ChooseLocationUiState(hasPreciseLocationPermission = true))
}

@Composable
@Preview(name = "Location selected", showBackground = true)
@Preview(
    name = "Location selected - dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private fun SelectedChooseLocationScreenPreview() {
    ChooseLocationPreview(
        ChooseLocationUiState(location = previewLocation, hasPreciseLocationPermission = true)
    )
}

@Composable
@Preview(name = "Selected - permission required", showBackground = true)
private fun SelectedWithoutPermissionChooseLocationScreenPreview() {
    ChooseLocationPreview(ChooseLocationUiState(location = previewLocation))
}

@Composable
@Preview(name = "Finding location", showBackground = true)
private fun LoadingChooseLocationScreenPreview() {
    ChooseLocationPreview(ChooseLocationUiState(isLoading = true, hasPreciseLocationPermission = true))
}

@Composable
@Preview(name = "Updating location", showBackground = true)
private fun UpdatingChooseLocationScreenPreview() {
    ChooseLocationPreview(
        ChooseLocationUiState(
            location = previewLocation,
            isLoading = true,
            hasPreciseLocationPermission = true
        )
    )
}

@Composable
@Preview(name = "Location error", showBackground = true)
private fun ErrorChooseLocationScreenPreview() {
    ChooseLocationPreview(
        ChooseLocationUiState(
            hasPreciseLocationPermission = true,
            error = ChooseLocationError.LOCATION_UNAVAILABLE
        )
    )
}

@Composable
@Preview(name = "Permission denied", showBackground = true)
private fun PermissionDeniedChooseLocationScreenPreview() {
    ChooseLocationPreview(
        ChooseLocationUiState(
            error = ChooseLocationError.PRECISE_PERMISSION_REQUIRED
        )
    )
}

@Composable
@Preview(
    name = "Long city name - large text",
    showBackground = true,
    widthDp = 320,
    fontScale = 1.5f
)
private fun LargeTextChooseLocationScreenPreview() {
    ChooseLocationPreview(
        ChooseLocationUiState(
            location = previewLocation.copy(displayName = "Petropavlovsk-Kamchatsky"),
            hasPreciseLocationPermission = true
        )
    )
}
