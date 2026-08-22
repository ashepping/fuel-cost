package com.ashepping.fuelcost.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class FormulaTest {
    @Test
    fun corollaHighwayAc() {
        val out = Formula.estimate(
            EstimateInput(
                cityL100 = 6.4,
                highwayL100 = 4.8,
                mixedL100 = null,
                year = 2016,
                distanceKm = 120.0,
                road = Road.HIGHWAY,
                acOn = true,
                heatOn = false,
                pricePerLiter = 1.65
            )
        )!!
        assertEquals("6.4", String.format(java.util.Locale.US, "%.1f", out.liters).let { out.litersText.take(3) })
        // 4.8 * 1.105 * 1.08 * 1.20 = 6.8515 → ~6.9 л, cost ~11.30
        assertEquals(6.9, kotlin.math.round(out.liters * 10) / 10.0, 0.05)
    }
}
