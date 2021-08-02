package ua.hospes.rtm.ui.cars

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.theme.RTMTheme
import ua.hospes.rtm.utils.compose.rememberFlowWithLifecycle

@Composable
fun Cars(
    openEditCar: (item: Car?) -> Unit
) {
    Cars(
        viewModel = hiltViewModel(),
        openEdit = openEditCar
    )
}

@Composable
internal fun Cars(
    viewModel: CarsViewModel,
    openEdit: (item: Car?) -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.cars)
        .collectAsState(initial = emptyList())

    Cars(items = viewState, openEdit = openEdit)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Cars(
    items: List<Car>,
    openEdit: (item: Car?) -> Unit
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(
            items = items
        ) {
            ItemCell(item = it, openEdit = openEdit)
        }
    }
}

@Composable
private fun ItemCell(
    item: Car,
    openEdit: (item: Car?) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { openEdit.invoke(item) }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = item.number.toString(),
            color = when (item.broken) {
                true -> Color.Gray
                false -> colorResource(item.quality.color)
            },
            fontSize = 52.sp
        )

        if (item.broken)
            Text(
                text = stringResource(R.string.cars_broken),
                color = colorResource(R.color.car_quality_low),
                modifier = Modifier.align(Alignment.TopEnd)
            )
    }
}


@Preview
@Composable
private fun PreviewScreen() {
    RTMTheme {
        Cars(
            listOf(
                Car(0, 1),
                Car(0, 2),
                Car(0, 3),
                Car(0, 4),
                Car(0, 5),
                Car(id = 0, number = 10, quality = Car.Quality.LOW, broken = true),
                Car(id = 0, number = 13, quality = Car.Quality.LOW, broken = false),
                Car(id = 0, number = 21, quality = Car.Quality.HIGH, broken = false)
            )
        ) {}
    }
}