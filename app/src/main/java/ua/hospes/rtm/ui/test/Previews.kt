package ua.hospes.rtm.ui.test

import androidx.compose.runtime.Composable
import androidx.ui.tooling.preview.Preview
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.theme.RTMTheme

@Preview
@Composable
fun previewCars() {
    RTMTheme {
        BodyContent(listOf(
                Car(0, 1),
                Car(0, 2),
                Car(0, 3),
                Car(0, 4),
                Car(0, 5),
                Car(0, 10),
                Car(0, 13),
                Car(0, 21)
        ))
    }
}

@Preview
@Composable
fun previewCarCell() {
    RTMTheme {
        CarCell(car = Car(0, 10))
    }
}