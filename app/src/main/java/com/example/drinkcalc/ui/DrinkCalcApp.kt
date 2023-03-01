package com.example.drinkcalc.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items as trueItems
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun DrinkCalcApp(
    modifier: Modifier = Modifier,
    calcViewModel: DrinkCalcViewModel = viewModel()
) {
    calcViewModel.updator
    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NavigationBar(calcViewModel = calcViewModel)
        when (calcViewModel.getCurrentWindow()) {
            State.NAMES ->
                NamesWindow(calcViewModel = calcViewModel)
            State.CATEGORIES ->
                CategoriesWindow(calcViewModel = calcViewModel)
            State.RESULTS ->
                ResultsWindow(calcViewModel = calcViewModel)
        }
    }
}

@Composable
fun NavigationButton(
    state: com.example.drinkcalc.ui.State,
    @StringRes resourseName: Int,
    modifier: Modifier = Modifier,
    calcViewModel: DrinkCalcViewModel = viewModel()
) {
    Button(
        modifier = modifier
            .padding(start = 8.dp),
        onClick = { calcViewModel.openWindow(state) }
    ) {
        Text(stringResource(resourseName))
    }
}

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    calcViewModel: DrinkCalcViewModel = viewModel()
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxHeight(0.1f)
    ) {
        NavigationButton(
            state = State.NAMES,
            resourseName = com.example.drinkcalc.R.string.towarisches,
            calcViewModel = calcViewModel
        )
        NavigationButton(
            state = State.CATEGORIES,
            resourseName = com.example.drinkcalc.R.string.categories,
            calcViewModel = calcViewModel,
        )
        NavigationButton(
            state = State.RESULTS,
            resourseName = com.example.drinkcalc.R.string.results,
            calcViewModel = calcViewModel,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultsWindow(
    modifier: Modifier = Modifier,
    calcViewModel: DrinkCalcViewModel = viewModel()
) {
    val listForPrint = calcViewModel.CalcSpending()
    LazyColumn{
        listForPrint.forEach {
            stickyHeader {
                Text(
                    text = it.key,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .wrapContentWidth(align = Alignment.CenterHorizontally),
                )
            }

            trueItems(it.value.toList()) { tow ->
                Row {
                    Text(
                        text = tow.second.toString(),
                        modifier = modifier
                            .fillMaxWidth(0.3f)
                            .padding(8.dp),
                    )
                    Text(
                        text = " -> ",
                        modifier = modifier
                            .fillMaxWidth(0.2f)
                            .padding(8.dp),
                    )
                    Text(
                        text = tow.first,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TowCard(name = "Serega", spend = 20U)
}