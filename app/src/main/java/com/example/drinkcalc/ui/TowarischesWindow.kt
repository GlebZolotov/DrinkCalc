package com.example.drinkcalc.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.items as trueItems
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drinkcalc.R

@Composable
fun InputTowRow(
    curName: String,
    onNameChanged: (String) -> Unit,
    curSpend: String,
    onSpendChanged: (String) -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row (
        modifier = modifier
            .wrapContentHeight(
                align = Alignment.CenterVertically
            )
    ){
        OutlinedTextField(
            value = curName,
            singleLine = true,
            modifier = modifier
                .fillMaxWidth(0.4f)
                .padding(8.dp),
            onValueChange = onNameChanged,
            label = { Text(stringResource(R.string.towarisch)) },
        )
        OutlinedTextField(
            value = curSpend,
            singleLine = true,
            modifier = modifier
                .fillMaxWidth(0.6f)
                .padding(8.dp),
            onValueChange = onSpendChanged,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            label = { Text(stringResource(R.string.potracheno)) },
        )
        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = onFinish
        ) {
            Text(stringResource(R.string.add))
        }
    }
}

@Composable
fun TowCard(name: String, spend: UInt,
            modifier: Modifier = Modifier,
            calcViewModel: DrinkCalcViewModel = viewModel()
) {
    Row {
        Text(
            text = name,
            modifier = modifier
                .fillMaxWidth(0.4f)
                .padding(8.dp),
        )
        Text(
            text = spend.toString(),
            modifier = modifier
                .fillMaxWidth(0.4f)
                .padding(8.dp),
        )
        Button(
            modifier = modifier
                .fillMaxWidth(0.1f)
                .weight(1f)
                .padding(8.dp),
            onClick = { calcViewModel.updTow(name) }
        ) {
            Text(stringResource(R.string.update))
        }
        Button(
            modifier = modifier
                .fillMaxWidth(0.1f)
                .weight(1f)
                .padding(8.dp),
            onClick = { calcViewModel.delTow(name) }
        ) {
            Text(stringResource(R.string.remove))
        }
    }
}

@Composable
fun NamesWindow(
    modifier: Modifier = Modifier,
    calcViewModel: DrinkCalcViewModel = viewModel()
) {
    InputTowRow(
        curName = calcViewModel.userTowName,
        onNameChanged = { calcViewModel.updTowName(it) },
        curSpend = calcViewModel.userTowSpend.toString(),
        onSpendChanged = { calcViewModel.updTowSpend(it) },
        onFinish = { calcViewModel.addTowarisch() },
        modifier = modifier,
    )
    val listForPrint = calcViewModel.getTowList()
    LazyColumn{
        trueItems(listForPrint){ tow ->
            TowCard(name = tow.first, spend = tow.second,
                calcViewModel = calcViewModel)
        }
    }
}