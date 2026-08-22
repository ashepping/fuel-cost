package com.ashepping.fuelcost.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ashepping.fuelcost.data.Cars
import com.ashepping.fuelcost.domain.Currency
import com.ashepping.fuelcost.domain.EstimateInput
import com.ashepping.fuelcost.domain.Formula
import com.ashepping.fuelcost.domain.Road

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelScreen() {
    var carId by rememberSaveable { mutableStateOf("corolla") }
    var customL by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("2016") }
    var km by rememberSaveable { mutableStateOf("120") }
    var road by rememberSaveable { mutableStateOf(Road.HIGHWAY.name) }
    var ac by rememberSaveable { mutableStateOf(true) }
    var heat by rememberSaveable { mutableStateOf(false) }
    var price by rememberSaveable { mutableStateOf("1.65") }
    var currency by rememberSaveable { mutableStateOf(Currency.EUR.code) }
    var result by rememberSaveable { mutableStateOf("") }
    var menuOpen by rememberSaveable { mutableStateOf(false) }
    var curOpen by rememberSaveable { mutableStateOf(false) }

    val selected = if (carId == Cars.CUSTOM_ID) null else Cars.byId(carId)
    val label = if (carId == Cars.CUSTOM_ID) "Свой расход" else selected?.title ?: "Toyota Corolla"

    fun recalc() {
        val distance = km.replace(',', '.').toDoubleOrNull() ?: return
        val p = price.replace(',', '.').toDoubleOrNull() ?: return
        val y = year.toIntOrNull()
        val custom = customL.replace(',', '.').toDoubleOrNull()
        val input = if (carId == Cars.CUSTOM_ID) {
            EstimateInput(
                cityL100 = null,
                highwayL100 = null,
                mixedL100 = custom,
                year = y,
                distanceKm = distance,
                road = Road.valueOf(road),
                acOn = ac,
                heatOn = heat,
                pricePerLiter = p
            )
        } else {
            val car = selected ?: return
            EstimateInput(
                cityL100 = car.cityL100,
                highwayL100 = car.highwayL100,
                mixedL100 = null,
                year = y,
                distanceKm = distance,
                road = Road.valueOf(road),
                acOn = ac,
                heatOn = heat,
                pricePerLiter = p
            )
        }
        val out = Formula.estimate(input)
        result = if (out == null) "" else "${out.litersText} л | ${out.costText} $currency"
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Fuel Cost", style = MaterialTheme.typography.headlineMedium)
            ExposedDropdownMenuBox(expanded = menuOpen, onExpandedChange = { menuOpen = it }) {
                OutlinedTextField(
                    value = label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Модель") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(menuOpen) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    DropdownMenuItem(
                        text = { Text("Свой расход") },
                        onClick = { carId = Cars.CUSTOM_ID; menuOpen = false; result = "" }
                    )
                    Cars.catalog.forEach { car ->
                        DropdownMenuItem(
                            text = { Text(car.title) },
                            onClick = { carId = car.id; menuOpen = false; result = "" }
                        )
                    }
                }
            }
            if (carId == Cars.CUSTOM_ID) {
                OutlinedTextField(
                    value = customL,
                    onValueChange = { customL = it },
                    label = { Text("Свой расход л/100") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            OutlinedTextField(
                value = year,
                onValueChange = { year = it.filter { ch -> ch.isDigit() }.take(4) },
                label = { Text("Год выпуска") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = km,
                onValueChange = { km = it },
                label = { Text("Дистанция, км") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = road == Road.CITY.name, onClick = { road = Road.CITY.name }, label = { Text("Город") })
                FilterChip(selected = road == Road.HIGHWAY.name, onClick = { road = Road.HIGHWAY.name }, label = { Text("Шоссе") })
                FilterChip(selected = road == Road.OFFROAD.name, onClick = { road = Road.OFFROAD.name }, label = { Text("Поле") })
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = ac, onClick = { ac = !ac }, label = { Text(if (ac) "AC вкл" else "AC выкл") })
                FilterChip(selected = heat, onClick = { heat = !heat }, label = { Text(if (heat) "Печка вкл" else "Печка выкл") })
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Цена литра") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                ExposedDropdownMenuBox(expanded = curOpen, onExpandedChange = { curOpen = it }, modifier = Modifier.weight(0.7f)) {
                    OutlinedTextField(
                        value = currency,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Валюта") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(curOpen) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = curOpen, onDismissRequest = { curOpen = false }) {
                        Currency.entries.forEach {
                            DropdownMenuItem(text = { Text(it.code) }, onClick = { currency = it.code; curOpen = false })
                        }
                    }
                }
            }
            Button(onClick = { recalc() }, modifier = Modifier.fillMaxWidth()) {
                Text("Посчитать")
            }
            if (result.isNotBlank()) {
                Surface(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), modifier = Modifier.fillMaxWidth()) {
                    Text(result, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                }
            }
            Text("Оценка расхода", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}
