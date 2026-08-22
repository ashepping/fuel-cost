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
        assertEquals("6.9", out.litersText)
        assertEquals("11.34", out.costText)
    }

    @Test
    fun ageCap() {
        val k = Formula.ageFactor(1980, 2026)
        assertEquals(1.30, k, 0.0001)
    }
}
