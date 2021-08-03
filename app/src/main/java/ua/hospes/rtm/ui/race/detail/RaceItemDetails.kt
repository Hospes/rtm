package ua.hospes.rtm.ui.race.detail

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.hospes.rtm.R
import ua.hospes.rtm.theme.RTMTheme
import ua.hospes.rtm.utils.compose.rememberFlowWithLifecycle

@Composable
fun RaceItemDetails(
    navigateUp: () -> Unit,
) {
    RaceItemDetails(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp
    )
}

@Composable
internal fun RaceItemDetails(
    viewModel: RaceItemDetailsViewModel,
    navigateUp: () -> Unit,
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = RaceItemDetailsViewState.Empty)

    RaceItemDetails(state = viewState) { action ->
        when (action) {
            RaceItemDetailsAction.Close -> navigateUp()
            else -> Unit //viewModel.submitAction(action)
        }
    }
}

@Composable
private fun RaceItemDetails(
    state: RaceItemDetailsViewState,
    actioner: (RaceItemDetailsAction) -> Unit
) {
    Scaffold(
        topBar = {
            RaceItemDetailsAppBar(
                title = state.item?.team?.name,
                actioner = actioner
            )
        }
    ) {

    }
}

@Composable
private fun RaceItemDetailsAppBar(
    title: String?,
    actioner: (RaceItemDetailsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(text = title ?: "Test")
        },
        navigationIcon = {
            IconButton(onClick = { actioner(RaceItemDetailsAction.Close) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.navigation_drawer_close),
                )
            }
        },
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.primary,
        modifier = modifier
    )
}

@Preview
@Composable
private fun PreviewScreen() {
    RTMTheme {
        RaceItemDetails(
            state = RaceItemDetailsViewState.Empty,
            actioner = {}
        )
    }
}