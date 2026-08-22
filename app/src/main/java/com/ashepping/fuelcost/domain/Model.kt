package com.ashepping.fuelcost.domain

enum class Road { CITY, HIGHWAY, OFFROAD }

enum class Currency(val code: String) {
    EUR("EUR"), USD("USD"), RUB("RUB")
}

data class Car(
    val id: String,
    val brand: String,
    val model: String,
    val cityL100: Double?,
    val highwayL100: Double?
) {
    val title: String get() = "$brand $model"
}

data class EstimateInput(
    val cityL100: Double?,
    val highwayL100: Double?,
    val mixedL100: Double?,
    val year: Int?,
    val distanceKm: Double,
    val road: Road,
    val acOn: Boolean,
    val heatOn: Boolean,
    val pricePerLiter: Double,
    val nowYear: Int = 2026
)

data class Estimate(
    val liters: Double,
    val cost: Double,
    val litersText: String,
    val costText: String
)
