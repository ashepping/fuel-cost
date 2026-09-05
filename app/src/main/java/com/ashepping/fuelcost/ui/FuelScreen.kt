package com.ashepping.fuelcost.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ashepping.fuelcost.data.Cars
import com.ashepping.fuelcost.data.Profile
import com.ashepping.fuelcost.data.ProfileStore
import com.ashepping.fuelcost.domain.Car
import com.ashepping.fuelcost.domain.Currency
import com.ashepping.fuelcost.domain.EstimateInput
import com.ashepping.fuelcost.domain.Formula
import com.ashepping.fuelcost.domain.Road

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelScreen() {
    val context = LocalContext.current
    val store = remember { ProfileStore(context) }
    val saved = remember { store.load() }

    var carId by rememberSaveable { mutableStateOf(saved.carId) }
    var customL by rememberSaveable { mutableStateOf(saved.customL) }
    var year by rememberSaveable { mutableStateOf(saved.year) }
    var km by rememberSaveable { mutableStateOf(saved.km) }
    var road by rememberSaveable { mutableStateOf(saved.road) }
    var ac by rememberSaveable { mutableStateOf(saved.ac) }
    var heat by rememberSaveable { mutableStateOf(saved.heat) }
    var price by rememberSaveable { mutableStateOf(saved.price) }
    var currency by rememberSaveable { mutableStateOf(saved.currency) }
    var menuOpen by rememberSaveable { mutableStateOf(false) }
    var curOpen by rememberSaveable { mutableStateOf(false) }
    var carQuery by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(carId, customL, year, km, road, ac, heat, price, currency) {
        store.save(Profile(carId, customL, year, km, road, ac, heat, price, currency))
    }

    val selected = if (carId == Cars.CUSTOM_ID) null else Cars.byId(carId)
    val label = if (carId == Cars.CUSTOM_ID) "Свой расход" else selected?.title ?: "Toyota Corolla"
    val resultText = computeResult(carId, selected, customL, year, km, road, ac, heat, price, currency)
    val carsShown = Cars.search(carQuery)

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Поехали", style = MaterialTheme.typography.headlineMedium)
            Surface(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(resultText, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
            }
            Text("Оценка расхода", style = MaterialTheme.typography.bodySmall)
            OutlinedTextField(
                value = carQuery,
                onValueChange = { carQuery = it },
                label = { Text("Поиск марки или модели") },
                modifier = Modifier.fillMaxWidth()
            )
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
                    DropdownMenuItem(text = { Text("Свой расход") }, onClick = { carId = Cars.CUSTOM_ID; menuOpen = false })
                    carsShown.forEach { car ->
                        DropdownMenuItem(text = { Text(car.title) }, onClick = { carId = car.id; menuOpen = false })
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
        }
    }
}

private fun parseNum(raw: String): Double? =
    raw.replace(",", ".").replace(" ", "").toDoubleOrNull()

private fun computeResult(
    carId: String,
    selected: Car?,
    customL: String,
    year: String,
    km: String,
    road: String,
    ac: Boolean,
    heat: Boolean,
    price: String,
    currency: String
): String {
    val distance = parseNum(km)
    val p = parseNum(price)
    if (distance == null || distance <= 0) return "Укажи дистанцию, км"
    if (p == null || p <= 0) return "Укажи цену литра"
    if (carId == Cars.CUSTOM_ID) {
        val mixed = parseNum(customL)
        if (mixed == null || mixed <= 0) return "Укажи свой расход л/100"
    } else if (selected == null) {
        return "Выбери модель"
    }
    val y = year.toIntOrNull()
    val roadEnum = runCatching { Road.valueOf(road) }.getOrDefault(Road.HIGHWAY)
    val input = if (carId == Cars.CUSTOM_ID) {
        EstimateInput(null, null, parseNum(customL), y, distance, roadEnum, ac, heat, p)
    } else {
        EstimateInput(selected!!.cityL100, selected.highwayL100, null, y, distance, roadEnum, ac, heat, p)
    }
    val out = Formula.estimate(input) ?: return "Не хватает данных"
    return "${out.litersText} л | ${out.costText} $currency"
}
