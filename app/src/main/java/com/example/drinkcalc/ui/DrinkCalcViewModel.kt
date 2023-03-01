package com.example.drinkcalc.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.drinkcalc.model.Category
import kotlin.math.absoluteValue

enum class State {NAMES, CATEGORIES, RESULTS}

class DrinkCalcViewModel : ViewModel() {
    private var towSpending: MutableMap<String, UInt> = mutableMapOf()
    private var categories: MutableSet<Category> = mutableSetOf()

    var currentAppState by mutableStateOf(State.NAMES)
        private set

    var userTowName by mutableStateOf("")
        private set
    var userTowSpend by mutableStateOf(0U)
        private set
    var userCatName by mutableStateOf("")
        private set
    var userCatSpend by mutableStateOf(0U)
        private set
    var userCatMembers: MutableSet<String> = mutableSetOf()
    var updator by mutableStateOf(true)

    fun resetApp() {
        towSpending.clear()
        categories.clear()

        userTowName = ""
        userTowSpend = 0U
        userCatName = ""
        userCatSpend = 0U
        userCatMembers = mutableSetOf()

        openWindow(State.NAMES)
    }

    init{
        resetApp()
    }

    fun update() {
        updator = !updator
    }

    fun openWindow(state: State) {
        currentAppState = state
    }

    fun getCurrentWindow(): State {
        return currentAppState
    }

    // towarisches methods
    fun getTowList(): List<Pair<String, UInt>> {
        return towSpending.toList()
    }

    fun updTowName(guessedWord: String){
        userTowName = guessedWord
    }

    fun updTowSpend(guessedNumb: String){
        var newVal = guessedNumb
        if (guessedNumb.length == 2 && guessedNumb[1] == '0' && userTowSpend == 0U) {
            newVal = guessedNumb.dropLast(1)
        }
        userTowSpend = if (newVal.isNotEmpty()) {
            newVal.toUInt()
        } else {
            0U
        }
    }

    fun updTow(name: String) {
        updTowName(name)
        updTowSpend(towSpending[name].toString())
    }

    fun delTow(name: String) {
        towSpending.remove(name)
        update()
    }

    fun addTowarisch() {
        towSpending[userTowName] = userTowSpend
        userTowName = ""
        userTowSpend = 0U
    }


    // category methods
    fun getCatList(): List<Category> {
        return categories.toList()
    }

    fun updCatName(guessedWord: String){
        userCatName = guessedWord
    }

    fun updCatSpend(guessedNumb: String){
        var newVal = guessedNumb
        if (guessedNumb.length == 2 && guessedNumb[1] == '0' && userCatSpend == 0U) {
            newVal = guessedNumb.dropLast(1)
        }
        val cur = if (newVal.isNotEmpty()) {
            newVal.toUInt()
        } else {
            0U
        }
        if (towSpending.values.sum().toInt() -
            categories.sumOf { it.totalCost }.toInt() - cur.toInt() > 0)
            userCatSpend = cur
    }

    fun getCurCatMembers(): List<String> {
        return userCatMembers.toList()
    }

    fun addCurCatMember(tow: String) {
        userCatMembers.add(tow)
        update()
    }

    fun delCurCatMember(tow: String) {
        userCatMembers.remove(tow)
        update()
    }

    fun getCurCatAvailableMembers(): List<String> {
        return towSpending.keys.toList() - userCatMembers
    }

    fun delTowFromCat(catName: String, name: String) {
        categories.single { it.name == catName }.towarisches.remove(name)
        update()
    }

    fun updCat(name: String) {
        val cat = categories.single { it.name == name }
        updCatName(name)
        updCatSpend(cat.totalCost.toString())
        userCatMembers = cat.towarisches.toMutableSet()
    }

    fun delCat(name: String) {
        categories.removeAll { it.name == name }
        update()
    }

    fun addCategory() {
        categories.add(Category(userCatName, userCatSpend, userCatMembers))
        userCatName = ""
        userCatSpend = 0U
        userCatMembers = mutableSetOf()
        update()
    }

    fun CalcSpending(): Map<String, MutableMap<String, UInt>> {
        val sumOfSpendingByOne: UInt =
            (towSpending.values.sum() - categories.sumOf { it.totalCost })
              .div(towSpending.size.toUInt())
        val towSumInCategories: Map<String, UInt> = towSpending.mapValues {
                item -> categories.filter {
                            it.towarisches.contains(item.key)
                        }.sumOf {
                            it.totalCost.div(it.towarisches.size.toUInt())
                        }
        }

        val towBalance: MutableList<Pair<String, Int>> = towSpending.mapValues {
            it.value.toInt() - sumOfSpendingByOne.toInt() - towSumInCategories.getOrElse(it.key) { 0u }.toInt()
        }.toList().sortedBy { it -> it.second }.toMutableList()
        Log.d("Calc", "towBalance: $towBalance")

        val towTransfers: Map<String, MutableMap<String, UInt>> =
            towSpending.mapValues { mutableMapOf() }

        while (towBalance.size > 1) {
            val insValue: Int = towBalance.first().second + towBalance.last().second
            val insIndex: Int = towBalance.indices.find { towBalance[it].second > insValue }!!
            if (towBalance.first().second.absoluteValue > towBalance.last().second) {
                towTransfers[towBalance.first().first]?.set(towBalance.last().first,
                    towBalance.last().second.toUInt()
                )
                towBalance.add(insIndex, Pair(towBalance.first().first, insValue))
            } else {
                towTransfers[towBalance.first().first]?.set(towBalance.last().first,
                    towBalance.first().second.absoluteValue.toUInt()
                )
                if (insValue != 0)
                    towBalance.add(insIndex, Pair(towBalance.last().first, insValue))
            }
            towBalance.removeLast()
            towBalance.removeFirst()
        }

        return towTransfers
    }
}