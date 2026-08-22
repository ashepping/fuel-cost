package com.ashepping.fuelcost.domain

import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

object Formula {
    fun estimate(input: EstimateInput): Estimate? {
        if (input.distanceKm <= 0 || input.pricePerLiter <= 0) return null
        val base = baseL100(input) ?: return null
        val ageK = ageFactor(input.year, input.nowYear)
        val acK = if (input.acOn) 1.08 else 1.0
        val heatK = if (input.heatOn) 1.05 else 1.0
        val per100 = base * ageK * acK * heatK
        val liters = per100 * input.distanceKm / 100.0
        val cost = liters * input.pricePerLiter
        return Estimate(
            liters = liters,
            cost = cost,
            litersText = oneDecimal(liters),
            costText = twoDecimal(cost)
        )
    }

    fun baseL100(input: EstimateInput): Double? {
        val city = input.cityL100
        val hwy = input.highwayL100
        if (city != null && hwy != null) {
            return when (input.road) {
                Road.HIGHWAY -> hwy
                Road.CITY -> city
                Road.OFFROAD -> city * 1.10
            }
        }
        val mixed = input.mixedL100 ?: city ?: hwy ?: return null
        return when (input.road) {
            Road.HIGHWAY -> mixed * 0.90
            Road.CITY -> mixed * 1.15
            Road.OFFROAD -> mixed * 1.25
        }
    }

    fun ageFactor(year: Int?, nowYear: Int): Double {
        if (year == null) return 1.0
        val age = min(25, max(0, nowYear - year))
        val k = 1.0 + 0.015 * max(0, age - 3)
        return min(1.30, k)
    }

    private fun oneDecimal(v: Double): String =
        ((round(v * 10.0) / 10.0)).toString()

    private fun twoDecimal(v: Double): String =
        String.format(java.util.Locale.US, "%.2f", v)
}
