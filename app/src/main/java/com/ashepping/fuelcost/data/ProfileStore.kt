package com.ashepping.fuelcost.data

import android.content.Context

data class Profile(
    val carId: String = "corolla",
    val customL: String = "",
    val year: String = "2016",
    val km: String = "120",
    val road: String = "HIGHWAY",
    val ac: Boolean = true,
    val heat: Boolean = false,
    val price: String = "1.65",
    val currency: String = "EUR",
    val look: String = "CURRENT"
)

class ProfileStore(context: Context) {
    private val prefs = context.getSharedPreferences("fuel_profile", Context.MODE_PRIVATE)

    fun load(): Profile = Profile(
        carId = prefs.getString("carId", "corolla") ?: "corolla",
        customL = prefs.getString("customL", "") ?: "",
        year = prefs.getString("year", "2016") ?: "2016",
        km = prefs.getString("km", "120") ?: "120",
        road = prefs.getString("road", "HIGHWAY") ?: "HIGHWAY",
        ac = prefs.getBoolean("ac", true),
        heat = prefs.getBoolean("heat", false),
        price = prefs.getString("price", "1.65") ?: "1.65",
        currency = prefs.getString("currency", "EUR") ?: "EUR",
        look = prefs.getString("look", "CURRENT") ?: "CURRENT"
    )

    fun save(p: Profile) {
        prefs.edit()
            .putString("carId", p.carId)
            .putString("customL", p.customL)
            .putString("year", p.year)
            .putString("km", p.km)
            .putString("road", p.road)
            .putBoolean("ac", p.ac)
            .putBoolean("heat", p.heat)
            .putString("price", p.price)
            .putString("currency", p.currency)
            .putString("look", p.look)
            .apply()
    }
}
