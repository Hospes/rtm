package ua.hospes.rtm.ui.drivers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.theme.RTMTheme
import ua.hospes.rtm.utils.compose.rememberFlowWithLifecycle

@Composable
fun Drivers(
    openEditDriver: (driver: Driver?) -> Unit
) {
    Drivers(
        viewModel = hiltViewModel(),
        openEditDriver = openEditDriver
    )
}

@Composable
internal fun Drivers(
    viewModel: DriversViewModel,
    openEditDriver: (driver: Driver?) -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.drivers)
        .collectAsState(initial = emptyList())

    Drivers(items = viewState, openEditDriver = openEditDriver)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Drivers(
    items: List<Driver>,
    openEditDriver: (driver: Driver?) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.drivers_name),
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(R.string.drivers_team),
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.fillMaxWidth(1f)
                )
            }
        }

        itemsIndexed(
            items = items,
            key = { _, item -> item.id }
        ) { i, item ->
            DriverCell(
                even = i % 2 == 0,
                driver = item,
                openEditDriver = openEditDriver
            )
        }
    }
}

@Composable
private fun DriverCell(
    even: Boolean = false,
    driver: Driver,
    openEditDriver: (driver: Driver?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { openEditDriver.invoke(driver) }
            .background(color = if (even) MaterialTheme.colors.surface else Color.Transparent)
            .padding(16.dp)
    ) {
        Text(
            text = driver.name,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.fillMaxWidth(0.5f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = driver.teamName ?: "No team",
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.fillMaxWidth(1f)
        )
    }
}

@Preview
@Composable
private fun PreviewDriverItem() {
    RTMTheme {
        Drivers(
            items = listOf(
                Driver(0, "Test driver"),
                Driver(1, "Test driver"),
                Driver(2, "Test driver"),
                Driver(3, "Test driver"),
                Driver(4, "Test driver"),
            ),
            openEditDriver = {}
        )
    }
}