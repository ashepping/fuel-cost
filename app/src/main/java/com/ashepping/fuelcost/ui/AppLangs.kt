package com.ashepping.fuelcost.ui

data class AppLang(val code: String, val shortCode: String)

object AppLangs {
    val all = listOf(
        AppLang("ru", "RUS"),
        AppLang("en", "ENG"),
        AppLang("es", "ESP"),
        AppLang("pt", "POR"),
        AppLang("de", "DEU"),
        AppLang("fr", "FRA"),
        AppLang("tr", "TUR"),
        AppLang("ar", "ARA")
    )

    fun of(code: String) = all.firstOrNull { it.code == code } ?: all.first()
}
