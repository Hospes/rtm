package ua.hospes.rtm.ui.test

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.ui.cars.StaggeredGrid

@Composable
fun BodyContent(items: List<Car>) {
    StaggeredGrid {
        for (item in items) {
            CarCell(car = item)
        }
    }
}

@Composable
fun CarCell(car: Car) {
    Text(text = car.number.toString())
}