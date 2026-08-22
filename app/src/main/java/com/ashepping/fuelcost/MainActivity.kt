package com.ashepping.fuelcost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ashepping.fuelcost.ui.FuelScreen
import com.ashepping.fuelcost.ui.FuelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FuelTheme { FuelScreen() }
        }
    }
}
