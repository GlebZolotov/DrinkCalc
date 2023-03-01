package com.example.drinkcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.drinkcalc.ui.DrinkCalcApp
import com.example.drinkcalc.ui.theme.DrinkCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrinkCalcTheme {
                DrinkCalcApp()
            }
        }
    }
}
