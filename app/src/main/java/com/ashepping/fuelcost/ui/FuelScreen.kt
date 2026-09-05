package com.ashepping.fuelcost.ui

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.ashepping.fuelcost.R
import com.ashepping.fuelcost.data.Cars
import com.ashepping.fuelcost.data.Profile
import com.ashepping.fuelcost.data.ProfileStore
import com.ashepping.fuelcost.domain.Car
import com.ashepping.fuelcost.domain.Currency
import com.ashepping.fuelcost.domain.EstimateInput
import com.ashepping.fuelcost.domain.Formula
import com.ashepping.fuelcost.domain.Road
import java.util.Locale

private fun wrapLang(base: Context, code: String): Context {
    val locale = Locale(code)
    val config = Configuration(base.resources.configuration)
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    return base.createConfigurationContext(config)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FuelScreen() {
    val rawContext = LocalContext.current
    val store = remember { ProfileStore(rawContext) }
    val saved = remember { store.load() }
    var lang by rememberSaveable { mutableStateOf(saved.lang) }
    val wrapped = remember(lang) { wrapLang(rawContext, lang) }
    CompositionLocalProvider(
        LocalContext provides wrapped,
        LocalLayoutDirection provides if (lang == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr
    ) {
        FuelScreenBody(store, saved, lang) { lang = it }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun FuelScreenBody(
    store: ProfileStore,
    saved: Profile,
    lang: String,
    onLang: (String) -> Unit
) {
    val focus = LocalFocusManager.current
    var carId by rememberSaveable { mutableStateOf(saved.carId) }
    var customL by rememberSaveable { mutableStateOf(saved.customL) }
    var year by rememberSaveable { mutableStateOf(saved.year) }
    var km by rememberSaveable { mutableStateOf(saved.km) }
    var road by rememberSaveable { mutableStateOf(saved.road) }
    var ac by rememberSaveable { mutableStateOf(saved.ac) }
    var heat by rememberSaveable { mutableStateOf(saved.heat) }
    var price by rememberSaveable { mutableStateOf(saved.price) }
    var currency by rememberSaveable { mutableStateOf(saved.currency) }
    var lookName by rememberSaveable {
        mutableStateOf(if (saved.look == "CURRENT") "NEURAL_DARK" else saved.look)
    }
    var brandOpen by rememberSaveable { mutableStateOf(false) }
    var modelOpen by rememberSaveable { mutableStateOf(false) }
    var curOpen by rememberSaveable { mutableStateOf(false) }
    var langOpen by rememberSaveable { mutableStateOf(false) }
    var brandQuery by rememberSaveable {
        mutableStateOf(Cars.byId(saved.carId)?.brand.orEmpty())
    }
    val look = runCatching { AppLook.valueOf(lookName) }.getOrDefault(AppLook.NEURAL_DARK)

    LaunchedEffect(carId, customL, year, km, road, ac, heat, price, currency, lookName, lang) {
        store.save(Profile(carId, customL, year, km, road, ac, heat, price, currency, lookName, lang))
    }

    val selected = if (carId == Cars.CUSTOM_ID) null else Cars.byId(carId)
    val brands = remember { Cars.catalog.map { it.brand }.distinct().sorted() }
    val q = brandQuery.trim()
    val brandMatches = if (q.isEmpty()) brands else brands.filter {
        it.startsWith(q, ignoreCase = true) || it.contains(q, ignoreCase = true)
    }
    val activeBrand = brands.firstOrNull { it.equals(q, ignoreCase = true) }
    val models = if (activeBrand == null) emptyList() else {
        Cars.catalog.filter { it.brand.equals(activeBrand, ignoreCase = true) }
    }
    val resultText = computeResult(
        carId, selected, customL, year, km, road, ac, heat, price, currency,
        stringResource(R.string.need_distance),
        stringResource(R.string.need_price),
        stringResource(R.string.need_custom),
        stringResource(R.string.need_model),
        stringResource(R.string.need_data),
        stringResource(R.string.unit_l)
    )

    fun pickBrand(brand: String) {
        brandQuery = brand
        brandOpen = false
        val first = Cars.catalog.firstOrNull { it.brand.equals(brand, ignoreCase = true) }
        if (selected?.brand.equals(brand, ignoreCase = true).not()) {
            carId = first?.id ?: Cars.CUSTOM_ID
        }
        modelOpen = true
        focus.clearFocus()
    }

    FuelTheme(look) {
        val menuColors = MenuDefaults.itemColors(
            textColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineMedium)
                        Text(stringResource(R.string.subtitle), style = MaterialTheme.typography.bodySmall)
                    }
                    Box {
                        IconButton(onClick = { langOpen = true }) {
                            LangMark(look)
                        }
                        if (langOpen) {
                            Popup(alignment = Alignment.TopCenter, onDismissRequest = { langOpen = false }) {
                                Surface(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    modifier = Modifier.width(56.dp)
                                ) {
                                    Column {
                                        AppLangs.all.forEach { item ->
                                            Text(
                                                item.shortCode,
                                                fontSize = 12.sp,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        onLang(item.code)
                                                        langOpen = false
                                                    }
                                                    .padding(vertical = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    IconButton(onClick = {
                        lookName = when (look) {
                            AppLook.CURRENT -> AppLook.NEURAL_DARK.name
                            AppLook.NEURAL_DARK -> AppLook.NEURAL_LIGHT.name
                            AppLook.NEURAL_LIGHT -> AppLook.CURRENT.name
                        }
                    }) {
                        ThemeMark(look)
                    }
                }

                ExposedDropdownMenuBox(expanded = brandOpen, onExpandedChange = { brandOpen = it }) {
                    OutlinedTextField(
                        value = brandQuery,
                        onValueChange = {
                            brandQuery = it
                            brandOpen = true
                            modelOpen = false
                        },
                        label = { Text(stringResource(R.string.brand)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(brandOpen) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            brandMatches.firstOrNull()?.let { pickBrand(it) }
                        }),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = brandOpen,
                        onDismissRequest = { brandOpen = false },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        if (brandMatches.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.brand_not_found)) },
                                onClick = { brandOpen = false },
                                colors = menuColors
                            )
                        } else {
                            brandMatches.forEach { brand ->
                                DropdownMenuItem(
                                    text = { Text(brand) },
                                    onClick = { pickBrand(brand) },
                                    colors = menuColors
                                )
                            }
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = modelOpen && activeBrand != null,
                    onExpandedChange = { open ->
                        if (activeBrand != null) modelOpen = open
                    }
                ) {
                    OutlinedTextField(
                        value = when {
                            activeBrand == null -> ""
                            carId == Cars.CUSTOM_ID -> stringResource(R.string.custom_use)
                            selected?.brand.equals(activeBrand, ignoreCase = true) -> selected?.model.orEmpty()
                            else -> ""
                        },
                        onValueChange = {},
                        readOnly = true,
                        enabled = activeBrand != null,
                        label = { Text(stringResource(R.string.model)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(modelOpen) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = modelOpen && activeBrand != null,
                        onDismissRequest = { modelOpen = false },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.custom_use)) },
                            onClick = { carId = Cars.CUSTOM_ID; modelOpen = false },
                            colors = menuColors
                        )
                        models.forEach { car ->
                            DropdownMenuItem(
                                text = { Text(car.model) },
                                onClick = { carId = car.id; modelOpen = false },
                                colors = menuColors
                            )
                        }
                    }
                }

                if (carId == Cars.CUSTOM_ID) {
                    OutlinedTextField(
                        value = customL,
                        onValueChange = { customL = it },
                        label = { Text(stringResource(R.string.custom_l)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it.filter { ch -> ch.isDigit() }.take(4) },
                    label = { Text(stringResource(R.string.year)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = km,
                    onValueChange = { km = it },
                    label = { Text(stringResource(R.string.distance)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(selected = road == Road.CITY.name, onClick = { road = Road.CITY.name }, label = { Text(stringResource(R.string.city)) })
                    FilterChip(selected = road == Road.HIGHWAY.name, onClick = { road = Road.HIGHWAY.name }, label = { Text(stringResource(R.string.highway)) })
                    FilterChip(selected = road == Road.OFFROAD.name, onClick = { road = Road.OFFROAD.name }, label = { Text(stringResource(R.string.offroad)) })
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(selected = ac, onClick = { ac = !ac }, label = { Text(stringResource(if (ac) R.string.ac_on else R.string.ac_off)) })
                    FilterChip(selected = heat, onClick = { heat = !heat }, label = { Text(stringResource(if (heat) R.string.heat_on else R.string.heat_off)) })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text(stringResource(R.string.price)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                    ExposedDropdownMenuBox(expanded = curOpen, onExpandedChange = { curOpen = it }, modifier = Modifier.weight(0.7f)) {
                        OutlinedTextField(
                            value = currency,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.currency)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(curOpen) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = curOpen,
                            onDismissRequest = { curOpen = false },
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Currency.entries.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.code) },
                                    onClick = { currency = it.code; curOpen = false },
                                    colors = menuColors
                                )
                            }
                        }
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(resultText, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                }
                Spacer(Modifier.padding(4.dp))
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
    currency: String,
    needDistance: String,
    needPrice: String,
    needCustom: String,
    needModel: String,
    needData: String,
    unitL: String
): String {
    val distance = parseNum(km)
    val p = parseNum(price)
    if (distance == null || distance <= 0) return needDistance
    if (p == null || p <= 0) return needPrice
    if (carId == Cars.CUSTOM_ID) {
        val mixed = parseNum(customL)
        if (mixed == null || mixed <= 0) return needCustom
    } else if (selected == null) {
        return needModel
    }
    val y = year.toIntOrNull()
    val roadEnum = runCatching { Road.valueOf(road) }.getOrDefault(Road.HIGHWAY)
    val input = if (carId == Cars.CUSTOM_ID) {
        EstimateInput(null, null, parseNum(customL), y, distance, roadEnum, ac, heat, p)
    } else {
        EstimateInput(selected!!.cityL100, selected.highwayL100, null, y, distance, roadEnum, ac, heat, p)
    }
    val out = Formula.estimate(input) ?: return needData
    return "${out.litersText} $unitL | ${out.costText} $currency"
}
