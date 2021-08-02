package ua.hospes.rtm.ui.teams

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
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.theme.RTMTheme
import ua.hospes.rtm.utils.compose.rememberFlowWithLifecycle

@Composable
fun Teams(
    openEditTeam: (item: Team?) -> Unit
) {
    Teams(
        viewModel = hiltViewModel(),
        openEdit = openEditTeam
    )
}

@Composable
internal fun Teams(
    viewModel: TeamsViewModel,
    openEdit: (item: Team?) -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.teams)
        .collectAsState(initial = emptyList())

    Teams(items = viewState, openEdit = openEdit)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Teams(
    items: List<Team>,
    openEdit: (item: Team?) -> Unit
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
                    text = stringResource(R.string.teams_name),
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(R.string.teams_drivers),
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
            ItemCell(
                even = i % 2 == 0,
                item = item,
                openEdit = openEdit
            )
        }
    }
}

@Composable
private fun ItemCell(
    even: Boolean = false,
    item: Team,
    openEdit: (item: Team?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { openEdit.invoke(item) }
            .background(color = if (even) MaterialTheme.colors.surface else Color.Transparent)
            .padding(16.dp)
    ) {
        Text(
            text = item.name,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.fillMaxWidth(0.5f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = item.drivers.map(Driver::name).joinToString(), //toString().replace("[\\[\\]]".toRegex(), ""),
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.fillMaxWidth(1f)
        )
    }
}

@Preview
@Composable
private fun PreviewScreen() {
    RTMTheme {
        Teams(
            items = listOf(
                Team(0, "Test driver"),
                Team(1, "Test driver"),
                Team(2, "Test driver"),
                Team(3, "Test driver"),
                Team(4, "Test driver"),
            ),
            openEdit = {}
        )
    }
}