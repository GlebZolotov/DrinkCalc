package com.example.drinkcalc.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.items as trueItems
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drinkcalc.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectTowMenu(
    calcViewModel: DrinkCalcViewModel = viewModel()
) {
    val listItems = calcViewModel.getCurCatAvailableMembers()

    if (listItems.isEmpty())
        return

    var selectedItem by remember {
        mutableStateOf(listItems[0])
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    // the box
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        // text field
        TextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.towarisch)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listItems.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(onClick = {
                    selectedItem = selectedOption
                    calcViewModel.addCurCatMember(selectedOption)
                    expanded = false
                }) {
                    Text(text = selectedOption)
                }
            }
        }
    }
}

@Composable
fun InputMembersColumn(
    modifier: Modifier = Modifier,
    calcViewModel: DrinkCalcViewModel = viewModel()
) {
    Column(
        modifier = modifier.fillMaxWidth(0.7f)
    ) {
        val listForPrint = calcViewModel.getCurCatMembers()
        LazyColumn{
            trueItems(listForPrint){ tow ->
                Row {
                    Text(
                        text = tow,
                        modifier = modifier.fillMaxWidth(0.5f)
                    )
                    Button(
                        modifier = modifier
                            .fillMaxWidth(),
                        onClick = { calcViewModel.delCurCatMember(tow) }
                    ) {
                        Text(stringResource(R.string.remove))
                    }
                }
            }
        }
        SelectTowMenu(calcViewModel)
    }
}

@Composable
fun InputCatRow(
    modifier: Modifier = Modifier,
    calcViewModel: DrinkCalcViewModel = viewModel()
) {
    Row (
        modifier = modifier
            .wrapContentHeight(
                align = Alignment.CenterVertically
            )
    ){
        OutlinedTextField(
            value = calcViewModel.userCatName,
            singleLine = true,
            modifier = modifier
                .fillMaxWidth(0.3f)
                .padding(8.dp),
            onValueChange = { calcViewModel.updCatName(it) },
            label = { Text(stringResource(R.string.category)) },
        )
        OutlinedTextField(
            value = calcViewModel.userCatSpend.toString(),
            singleLine = true,
            modifier = modifier
                .fillMaxWidth(0.3f)
                .padding(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            onValueChange = { calcViewModel.updCatSpend(it) },
            label = { Text(stringResource(R.string.spends)) },
        )
        InputMembersColumn(modifier, calcViewModel)
        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = { calcViewModel.addCategory() }
        ) {
            Text(stringResource(R.string.add))
        }
    }
}

@Composable
fun CatMemberCard(
    name: String,
    cat_name: String,
    modifier: Modifier = Modifier,
    calcViewModel: DrinkCalcViewModel = viewModel()
) {
    Row {
        Text(
            text = name,
            modifier = modifier
                .fillMaxWidth(0.5f)
                .padding(8.dp)
                .wrapContentWidth(align = Alignment.CenterHorizontally),
        )
        Button(
            modifier = modifier
                .fillMaxWidth(0.3f)
                .padding(8.dp),
            onClick = { calcViewModel.delTowFromCat(cat_name, name) }
        ) {
            Text(stringResource(R.string.remove))
        }
    }
}

@Composable
fun CatHeaderCard(
    name: String,
    totalCost: UInt,
    modifier: Modifier = Modifier,
    calcViewModel: DrinkCalcViewModel = viewModel()
) {
    Row{
        Text(
            text = name,
            modifier = modifier
                .fillMaxWidth(0.4f)
                .padding(8.dp),
        )
        Text(
            text = totalCost.toString(),
            modifier = modifier
                .fillMaxWidth(0.4f)
                .padding(8.dp),
        )
        Button(
            modifier = modifier
                .fillMaxWidth(0.5f)
                .weight(1f)
                .padding(8.dp),
            onClick = { calcViewModel.updCat(name) }
        ) {
            Text(stringResource(R.string.update))
        }
        Button(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp),
            onClick = { calcViewModel.delCat(name) }
        ) {
            Text(stringResource(R.string.remove))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesWindow(
    modifier: Modifier = Modifier,
    calcViewModel: DrinkCalcViewModel = viewModel()
) {
    InputCatRow(modifier, calcViewModel)
    val listForPrint = calcViewModel.getCatList()
    LazyColumn{
        listForPrint.forEach {
            stickyHeader {
                CatHeaderCard(it.name, it.totalCost, modifier, calcViewModel)
            }

            trueItems(it.towarisches.toList()) { tow ->
                CatMemberCard(tow, it.name, modifier, calcViewModel)
            }
        }
    }
}