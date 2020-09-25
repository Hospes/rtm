package ua.hospes.rtm.ui.test

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.theme.RTMTheme
import ua.hospes.rtm.ui.cars.StaggeredGrid

@Composable
fun ComposeTestScreen() {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        WithConstraints {
            Stack(modifier = Modifier.weight(1f)) {
                Surface {
                    ScrollableColumn(
                            modifier = Modifier.fillMaxSize(),
                            scrollState = scrollState
                    ) {
//                        ProfileHeader(
//                                scrollState,
//                                userData
//                        )
//                        UserInfoFields(userData, maxHeight)
                    }
                }
                ProfileFab(
                        extended = scrollState.value == 0f,
                        userIsMe = true,
                        modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}

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

@Composable
fun ProfileFab(extended: Boolean, userIsMe: Boolean, modifier: Modifier = Modifier) {
    key(userIsMe) { // Prevent multiple invocations to execute during composition
        FloatingActionButton(
                onClick = { /* TODO */ },
                modifier = modifier
                        .padding(16.dp)
                        .preferredHeight(48.dp)
                        .widthIn(min = 48.dp),
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
        ) {
            AnimatingFabContent(
                    icon = {
                        Icon(asset = if (userIsMe) Icons.Outlined.Create else Icons.Outlined.Chat)
                    },
                    text = {
                        Text(text = if (userIsMe) "Edit profile" else "Tratatat")
                    },
                    extended = extended

            )
        }
    }
}

@Preview
@Composable
fun previewDefault() {
    RTMTheme {
        ComposeTestScreen()
    }
}